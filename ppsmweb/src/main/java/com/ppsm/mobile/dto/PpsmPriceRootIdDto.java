package com.ppsm.mobile.dto;

import java.util.List;

/**
 * @Description:
 * @Author: LiuYiQiang
 * @Date: 16:38 2018/5/6
 */
public class PpsmPriceRootIdDto {

    private String rootId;
    private String rootName;
    private List<PpsmPriceDto> ppsmPriceDtoList;

    public String getRootId() {
        return rootId;
    }

    public void setRootId(String rootId) {
        this.rootId = rootId;
    }

    public String getRootName() {
        return rootName;
    }

    public void setRootName(String rootName) {
        this.rootName = rootName;
    }

    public List<PpsmPriceDto> getPpsmPriceDtoList() {
        return ppsmPriceDtoList;
    }

    public void setPpsmPriceDtoList(List<PpsmPriceDto> ppsmPriceDtoList) {
        this.ppsmPriceDtoList = ppsmPriceDtoList;
    }
}
