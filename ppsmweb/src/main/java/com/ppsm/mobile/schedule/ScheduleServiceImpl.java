package com.ppsm.mobile.schedule;

import com.ppsm.mobile.dao.PpsmHtmlDao;
import com.ppsm.mobile.dao.PpsmMonitorDao;
import com.ppsm.mobile.entity.PpsmHtml;
import com.ppsm.mobile.service.IJsonParseToHtmlService;
import com.ppsm.mobile.utils.ListUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @Description:
 * @Author: LiuYiQiang
 * @Date: 20:41 2018/4/26
 */
@Component
@Transactional(rollbackFor = Exception.class)
public class ScheduleServiceImpl {

    private Logger logger = LoggerFactory.getLogger(ScheduleServiceImpl.class);

    @Autowired
    IJsonParseToHtmlService jsonParseToHtmlService;

    @Autowired
    private PpsmMonitorDao ppsmMonitorDao;

    @Autowired
    private PpsmHtmlDao ppsmHtmlDao;

    public void setPpsmHtmlDao(PpsmHtmlDao ppsmHtmlDao) {
        this.ppsmHtmlDao = ppsmHtmlDao;
    }

    /**
     * @Description: 定时任务
     * @Author: LiuYiQiang
     * @Date: 22:36 2018/4/26
     */
    @Scheduled(cron = "0 */3 * * * ?")
    public void updatePpsmPrice(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        //如果Json有变化则更新数据库
        if(checkJsonUpdate()){
            jsonParseToHtmlService.loadHtml();
            Timestamp time = new Timestamp(System.currentTimeMillis());
            ppsmMonitorDao.insertMonitor(time);
        }
        long timeSpend = Calendar.getInstance().getTimeInMillis() - calendar.getTimeInMillis();
        logger.info("胖胖数码批量服务执行完成,耗时{}毫秒", timeSpend);
    }

    /**
     * @Description: 判断如果Json改变则返回true
     * @Author: LiuYiQiang
     * @Date: 21:24 2018/4/26
     */
    private Boolean checkJsonUpdate(){
        String json = getJson();
        String htmlNew = getHtml(json);
        PpsmHtml ppsmHtml = ppsmHtmlDao.queryHtml();

        if(ppsmHtml == null){
            ppsmHtmlDao.insertHtml(htmlNew);
            return true;
        }

        String htmlOld = ppsmHtml.getHtml();
        if(compareJson(htmlOld,htmlNew)){
            return false;
        }

        ppsmHtmlDao.updateHtmlById(ppsmHtml.getId(),htmlNew);
        return true;
    }

    /**
     * @Description: 判断两个Json串内容是否相同
     * @Author: LiuYiQiang
     * @Date: 22:11 2018/4/26
     */
    private boolean compareJson(String htmlOld,String htmlNew){
        List<String> oldList = parseHtmlToList(htmlOld);
        List<String> newList = parseHtmlToList(htmlNew);
        return ListUtils.compare(oldList,newList);
    }

    /**
     * 根据html获取数据
     */
    private List<String> parseHtmlToList(String html){
        Document doc = Jsoup.parse(html);
        List<String> list = parseHTML(doc);
        return list;
    }


    private List<String> parseHTML(Document doc){
        Elements divs = doc.select("div.cell.product-series-heading");
        Elements tables = doc.select("table");
        //遍历div
        List<String> list = new ArrayList();
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
                    StringBuilder key = new StringBuilder();
                    String priceTH = ths.get(thsI).text();
                    String priceTD = tds.get(thsI).text();
                    String country = div.text();
                    //list的内容为：版本+型号+颜色+价格
                    key.append(country).append(modelTD).append(priceTH).append(priceTD);
                    list.add(key.toString());
                }
            }
        }
        return list;
    }

    /**
     * 获取链接中的json数据
     * @return
     */
    private String getJson(){
        String json = "";
        StringBuilder jsonbuilder = new StringBuilder();
        BufferedReader in = null;
        try {
            URL urlObject = new URL("https://price.ppsm.club/getprice");
            URLConnection uc = urlObject.openConnection();
            in = new BufferedReader(new InputStreamReader(uc.getInputStream(), "UTF-8"));
            String inputLine = null;
            while ((inputLine = in.readLine()) != null) {
                jsonbuilder.append(inputLine);
            }
        } catch (MalformedURLException e) {
            logger.error("URL解析出错",e);
        }catch (IOException e) {
            logger.error(" ",e);
        } finally {
            try{
                in.close();
            }catch (IOException e) {
                logger.error(" ",e);
            }
        }
        json = jsonbuilder.toString();
        return json;
    }

    /**
     * 获取json数据中的html数据
     */
    private String getHtml(String json){
        String html = "";
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject  data = jsonObject.getJSONObject("result");
            html = data.getString("html");
        } catch (JSONException e) {
            logger.error("JSON解析出错",e);
        }
        return html;
    }

}
