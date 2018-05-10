package com.ppsm.mobile.web;

import com.ppsm.mobile.dto.PpsmPriceDto;
import com.ppsm.mobile.dto.PpsmPriceRootIdDto;
import com.ppsm.mobile.service.IPpsmPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @Description:
 * @Author: LiuYiQiang
 * @Date: 20:11 2018/5/5
 */
@Controller
@RequestMapping("/ppsm")
public class PpsmController {

    @Autowired
    IPpsmPriceService ppsmPriceService;

    private static final String SUCCESS = "success";

    @RequestMapping(value = "/getPriceAll",produces="application/json;charset=utf-8",method = RequestMethod.GET)
    @ResponseBody
    public List<PpsmPriceDto> getPriceAll(){
        List<PpsmPriceDto> ppsmPriceDtos =  ppsmPriceService.getPpsmPriceAll();
        return ppsmPriceDtos;
    }

    @RequestMapping(value = "/getPriceAllForRootId",produces="application/json;charset=utf-8",method = RequestMethod.GET)
    @ResponseBody
    public List<PpsmPriceRootIdDto> getPriceAllForRootId(){
        List<PpsmPriceRootIdDto> ppsmPriceRootIdDtos = ppsmPriceService.getPriceForRootIdAll();
        return ppsmPriceRootIdDtos;
    }

}
