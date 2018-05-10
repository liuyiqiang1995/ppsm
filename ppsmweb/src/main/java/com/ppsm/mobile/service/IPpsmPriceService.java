package com.ppsm.mobile.service;

import com.ppsm.mobile.dto.PpsmPriceDto;
import com.ppsm.mobile.dto.PpsmPriceRootIdDto;

import java.util.List;

/**
 * @Description:
 * @Author: LiuYiQiang
 * @Date: 20:13 2018/5/5
 */
public interface IPpsmPriceService {

    List<PpsmPriceDto> getPpsmPriceAll();

    List<PpsmPriceRootIdDto> getPriceForRootIdAll();
}
