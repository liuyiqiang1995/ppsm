package com.ppsm.mobile.service.impl;

import com.ppsm.mobile.dao.MobilePriceDao;
import com.ppsm.mobile.dao.ProductRelationDao;
import com.ppsm.mobile.dto.PpsmPriceDto;
import com.ppsm.mobile.dto.PpsmPriceRootIdDto;
import com.ppsm.mobile.entity.MobilePrice;
import com.ppsm.mobile.entity.ProductRelation;
import com.ppsm.mobile.service.IPpsmPriceService;
import com.ppsm.mobile.utils.CopyEntityToDtoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: LiuYiQiang
 * @Date: 20:14 2018/5/5
 */
@Service
public class PpsmPriceServiceImpl implements IPpsmPriceService {

    private static Logger logger = LoggerFactory.getLogger(PpsmPriceServiceImpl.class);

    @Autowired
    MobilePriceDao mobilePriceDao;
    @Autowired
    ProductRelationDao productRelationDao;

    public List<PpsmPriceDto> getPpsmPriceAll() {
        List<MobilePrice> mobilePrices = mobilePriceDao.queryPriceAll();
        List<PpsmPriceDto> ppsmPriceDtos = new ArrayList<PpsmPriceDto>();
        for(MobilePrice mobilePrice : mobilePrices){
            PpsmPriceDto ppsmPriceDto = new PpsmPriceDto();
            CopyEntityToDtoUtils.copyMobilePriceToPpsmPriceDto(mobilePrice,ppsmPriceDto);
            ppsmPriceDtos.add(ppsmPriceDto);
        }
        return ppsmPriceDtos;
    }

    public List<PpsmPriceRootIdDto> getPriceForRootIdAll() {
        List<ProductRelation> productRelations = productRelationDao.queryAll();
        List<PpsmPriceRootIdDto> ppsmPriceRootIdDtos = new ArrayList<PpsmPriceRootIdDto>();
        for(ProductRelation productRelation : productRelations){
            PpsmPriceRootIdDto ppsmPriceRootIdDto = new PpsmPriceRootIdDto();
            List<MobilePrice> mobilePrices = mobilePriceDao.queryPriceByRootId(String.valueOf(productRelation.getId()));
            CopyEntityToDtoUtils.copyMobilePriceListToPpsmPriceRootIdDto(mobilePrices,ppsmPriceRootIdDto);
            ppsmPriceRootIdDtos.add(ppsmPriceRootIdDto);
        }
        return ppsmPriceRootIdDtos;
    }
}
