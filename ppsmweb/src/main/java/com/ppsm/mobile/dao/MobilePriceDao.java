package com.ppsm.mobile.dao;

import com.ppsm.mobile.entity.MobilePrice;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface MobilePriceDao {

    /**
     * 通过ID查询
     */
    MobilePrice queryById(int id);

    /**
     * 批量插入
     */
    void insertBatch(List<MobilePrice> list);

    /**
     * 更新ispublish
     */
    void updateIspublish();

    /**
     * @Description: 删除当前日期所有记录
     * @Author: LiuYiQiang
     * @Date: 20:46 2018/5/5
     */
    void deleteByDate(Date date);

    List<MobilePrice> queryPriceAll();

    List<MobilePrice> queryPriceByRootId(String rootId);
}
