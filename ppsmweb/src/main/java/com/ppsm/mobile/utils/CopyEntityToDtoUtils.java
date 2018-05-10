package com.ppsm.mobile.utils;

import com.ppsm.mobile.dto.PpsmPriceDto;
import com.ppsm.mobile.dto.PpsmPriceRootIdDto;
import com.ppsm.mobile.entity.MobilePrice;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: LiuYiQiang
 * @Date: 15:54 2018/5/6
 */
public class CopyEntityToDtoUtils {

    public static void copyMobilePriceToPpsmPriceDto(MobilePrice mobilePrice, PpsmPriceDto ppsmPriceDto){
        ppsmPriceDto.setId(mobilePrice.getId());
        ppsmPriceDto.setPrice(mobilePrice.getPrice());
        ppsmPriceDto.setTime(mobilePrice.getTime());
        ppsmPriceDto.setProductName(mobilePrice.getMobileProduct() == null ? "" : mobilePrice.getMobileProduct().getProductName());
        ppsmPriceDto.setProductColor(mobilePrice.getMobileProduct() == null ? "" : mobilePrice.getMobileProduct().getProductColor());
        ppsmPriceDto.setProductCountry(mobilePrice.getMobileProduct() == null ? "" : mobilePrice.getMobileProduct().getProductCountry());
    }

    public static void copyMobilePriceListToPpsmPriceRootIdDto(List<MobilePrice> mobilePrices, PpsmPriceRootIdDto ppsmPriceRootIdDto){
        List<PpsmPriceDto> ppsmPriceDtos = new ArrayList<PpsmPriceDto>();
        for(MobilePrice mobilePrice : mobilePrices){
            PpsmPriceDto ppsmPriceDto = new PpsmPriceDto();
            copyMobilePriceToPpsmPriceDto(mobilePrice,ppsmPriceDto);
            ppsmPriceDtos.add(ppsmPriceDto);
        }
        String rootId = (mobilePrices.get(0).getMobileProduct() == null ? "null" : mobilePrices.get(0).getMobileProduct().getRootId());
        String rootName = (mobilePrices.get(0).getMobileProduct() == null ? "null" : mobilePrices.get(0).getMobileProduct().getProductName());
        ppsmPriceRootIdDto.setRootId(rootId);
        ppsmPriceRootIdDto.setRootName(rootName);
        ppsmPriceRootIdDto.setPpsmPriceDtoList(ppsmPriceDtos);
    }

}
