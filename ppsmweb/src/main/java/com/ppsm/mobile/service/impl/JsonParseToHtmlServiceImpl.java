package com.ppsm.mobile.service.impl;

import com.ppsm.mobile.dao.MobilePriceDao;
import com.ppsm.mobile.dao.MobileProductDao;
import com.ppsm.mobile.dao.PpsmHtmlDao;
import com.ppsm.mobile.dao.ProductRelationDao;
import com.ppsm.mobile.entity.MobilePrice;
import com.ppsm.mobile.entity.MobileProduct;
import com.ppsm.mobile.entity.PpsmHtml;
import com.ppsm.mobile.entity.ProductRelation;
import com.ppsm.mobile.service.IJsonParseToHtmlService;
import com.ppsm.mobile.utils.DateStringUtil;
import com.ppsm.mobile.utils.ListUtils;
import org.apache.commons.collections.MapUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class JsonParseToHtmlServiceImpl implements IJsonParseToHtmlService {

	private Logger logger = LoggerFactory.getLogger(JsonParseToHtmlServiceImpl.class);

	@Autowired
	private PpsmHtmlDao ppsmHtmlDao;

	@Autowired
	private MobilePriceDao mobilePriceDao;

	@Autowired
	private MobileProductDao mobileProductDao;

	@Autowired
	private ProductRelationDao productRelationDao;

	@Transactional(rollbackFor = Exception.class)
	public void loadHtml(){
		PpsmHtml ppsmHtml = ppsmHtmlDao.queryHtml();
		List<Map<String, String>> list = parseHtmlToList(ppsmHtml.getHtml());
		ppsmBatch(list);
	}

	private void ppsmBatch(List<Map<String, String>> list){
		try {
			Date date = DateStringUtil.convertString(DateStringUtil.formatDate(new Date(),"yyyy-MM-dd"),"yyyy-MM-dd");
			mobilePriceDao.deleteByDate(date);
			mobilePriceDao.updateIspublish();
			insertProductRelation(list);
			insertMobileProduct(list);
			insertMobilePrice(list);
		}catch (ParseException e){
			logger.error("Data解析错误",e);
		}

	}

	private void insertProductRelation(List<Map<String, String>> list){
		List<ProductRelation> productRelations = productRelationDao.queryAll();
		List<String> productNames = new ArrayList<String>();
		for(ProductRelation productRelation : productRelations){
			String productName = "";
			productName = productRelation.getProductName();
			productNames.add(productName);
		}
		List<ProductRelation> needInsertList = new ArrayList<ProductRelation>();
		for(int i = 0; i < list.size(); i++){
			Map<String, String> map = list.get(i);
			String productName = map.get("name");
			if(!productNames.contains(productName)){
				ProductRelation productRelation = new ProductRelation();
				productRelation.setProductName(productName);
				needInsertList.add(productRelation);
				productNames.add(map.get("name"));
			}
		}
		if(ListUtils.isNotEmpty(needInsertList)){
			productRelationDao.insertBatch(needInsertList);
		}
	}

	private void insertMobileProduct(List<Map<String, String>> list){
		List<MobileProduct> mobileProducts = mobileProductDao.queryAll();
		List<ProductRelation> relations = productRelationDao.queryAll();
		List<String> mobileProductKeys = new ArrayList<String>();
		for(MobileProduct mobileProduct : mobileProducts){
			StringBuilder str= new StringBuilder();
			str.append(mobileProduct.getProductName()).append(mobileProduct.getProductColor()).append(mobileProduct.getProductCountry());
			mobileProductKeys.add(str.toString());
		}

		int rootId = -1;
		List<MobileProduct> needInsertList = new ArrayList<MobileProduct>();
		for (int i = 0; i < list.size(); i++) {
			Map<String, String> map = list.get(i);
			StringBuilder key = new StringBuilder();
			key.append(map.get("name")).append(map.get("color")).append(map.get("country"));
			if(!mobileProductKeys.contains(key.toString())){
				MobileProduct mobileProduct = new MobileProduct();
				for(ProductRelation productRelation : relations){
					if(map.get("name").equals(productRelation.getProductName())){
						rootId = productRelation.getId();
						break;
					}
				}
				mobileProduct.setRootId(String.valueOf(rootId));
				mobileProduct.setProductName(map.get("name"));
				mobileProduct.setProductColor(map.get("color"));
				mobileProduct.setProductCountry(map.get("country"));
				needInsertList.add(mobileProduct);
				mobileProductKeys.add(map.get("name"));
			}
		}
		if(ListUtils.isNotEmpty(needInsertList)){
			mobileProductDao.insertBatch(needInsertList);
		}
	}

	private void insertMobilePrice(List<Map<String, String>> list){
		List<MobilePrice> needInsertList = new ArrayList<MobilePrice>();
		Map<String,Integer> productMap = getAllProductId();
		StringBuilder key= new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			Map<String, String> map = list.get(i);
			MobilePrice mobilePrice = new MobilePrice();
			key.append(map.get("name")).append(map.get("color")).append(map.get("country"));
			mobilePrice.setProductId(getProductIdByKey(productMap,key.toString()));
			mobilePrice.setPrice(map.get("price"));
			mobilePrice.setTime(map.get("time"));
			Date date = new Date();
			java.sql.Date sqlDate = new java.sql.Date(date.getTime());
			mobilePrice.setDate(sqlDate);
			mobilePrice.setIspublish(0);
			needInsertList.add(mobilePrice);
		}
		if(ListUtils.isNotEmpty(needInsertList)){
			mobilePriceDao.insertBatch(needInsertList);
		}
	}

	private int getProductIdByKey(Map<String,Integer> map,String key){
		if(MapUtils.isEmpty(map)){
			return -1;
		}
		for(Map.Entry entry : map.entrySet()){
			if(entry.getKey().equals(key)){
				return (Integer) entry.getValue();
			}
		}
		return -1;
	}

	private Map<String,Integer> getAllProductId(){
		List<MobileProduct> mobileProducts = mobileProductDao.queryAll();
		Map<String,Integer> mobileProductMap = new HashMap<String, Integer>();
		StringBuilder str= new StringBuilder();
		for(MobileProduct mobileProduct : mobileProducts){
			str.append(mobileProduct.getProductName()).append(mobileProduct.getProductColor()).append(mobileProduct.getProductCountry());
			mobileProductMap.put(str.toString(),mobileProduct.getId());
		}
		return mobileProductMap;
	}

	/**
	 * 根据html获取数据
	 */
	private List<Map<String, String>> parseHtmlToList(String html){
		Document doc = Jsoup.parse(html);
		List<Map<String, String>> list = parseHTML(doc);
		return list;
	}

	private String checkJsonKH(String html){
		if(html.startsWith("{") && html.endsWith("}")){
			return html;
		}else if(html.startsWith("{") && !html.endsWith("}")){
			StringBuilder stringBuilder = new StringBuilder(html);
			stringBuilder.append("}");
			return stringBuilder.toString();
		}else if(!html.startsWith("{") && html.endsWith("}")){
			StringBuilder stringBuilder = new StringBuilder(html);
			stringBuilder.insert(0,"{");
			return stringBuilder.toString();
		}else{
			StringBuilder stringBuilder = new StringBuilder(html);
			stringBuilder.insert(0,"{");
			stringBuilder.append("}");
			return stringBuilder.toString();
		}
	}


	private List<Map<String, String>> parseHTML(Document doc){
		Elements divs = doc.select("div.cell.product-series-heading");
		Elements tables = doc.select("table");
		//遍历div
		List<Map<String, String>> list = new ArrayList();
		for(int divI = 0;divI < divs.size();divI++) {
			Element div = divs.get(divI);
			//根据div获取对应的table
			Element table = tables.get(divI);
			//获取table中的tr标签
			Elements trs = table.select("tr");
			//第一个tr标签里面装的是th
			Element tr1 = trs.get(0);
			Elements ths = tr1.select("th");
			//从第二个开始，遍历tr
			for (int i = 1; i < trs.size(); i++) {
				Element tr = trs.get(i);
				Elements tds = tr.select("td");
				String modelTD = tds.get(0).text();
				String timeTD = tds.get(tds.size() - 1).text();
				//遍历th
				for (int thsI = 1; thsI < ths.size() - 1; thsI++) {
					Map<String, String> map = new HashMap();
					String priceTH = ths.get(thsI).text();
					String priceTD = tds.get(thsI).text();
					String country = div.text();
					map.put("country", parseCountry(country));
					map.put("name", parseName(modelTD,country));
					map.put("color",priceTH);
					map.put("price", parsePrice(priceTD));
					map.put("time", parseTime(timeTD));
					list.add(map);
				}
			}
		}
		return list;
	}

	private String parsePrice(String price){
		if(price.indexOf("(") != -1){
			price = price.substring(0,price.indexOf("("));
		}
		return price;
	}

	/**
	 * @Description: 解析时间
	 * @Author: LiuYiQiang
	 * @Date: 20:27 2018/5/1
	 */
	private String parseTime(String time){
		String tOrigin = time;
		time = time.substring(0,1);
		String s = time;
		try {
			int t = Integer.parseInt(time);
			Date date = new Date();
			SimpleDateFormat df=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Calendar rightNow = Calendar.getInstance();
			rightNow.setTime(date);
			if(tOrigin.indexOf("小时") != -1){
				rightNow.set(Calendar.HOUR_OF_DAY,-t);
			}else if(tOrigin.indexOf("分钟") != -1){
				rightNow.set(Calendar.MINUTE,-t);
			}else{
				rightNow.set(Calendar.HOUR_OF_DAY,-t);
			}
			s = df.format(rightNow.getTime());
		}catch (Exception e){
			return tOrigin;
		}
		return s;
	}

	private String parseName(String name,String country){
		StringBuilder coun = new StringBuilder();
		int index = country.indexOf("寸");
		if(index != -1){
			coun.append(country.substring(0,index+2)).append(name);
			return coun.toString();
		}
		return name;
	}

	private String parseCountry(String country){
		if(country.indexOf("国行") != -1){
			country = "国行";
		}
		if(country.indexOf("港行") != -1){
			country = "港行";
		}
		if(country.indexOf("港版") != -1){
			country = "港版";
		}
		return country;
	}
}
