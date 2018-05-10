package com.ppsm.mobile.dao;

import com.ppsm.mobile.entity.MobileProduct;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MobileProductDao {

    /**
     * 通过ID查询单个型号
     */
    MobileProduct queryById(@Param("id") int id);

    /**
     * 查询所有型号
     */
    List<MobileProduct> queryAll();

    /**
     * 批量插入
     */
    void insertBatch(List<MobileProduct> list);

    /**
     * 根据业务主键查询Id
     */
    MobileProduct getIdByKey(@Param("productName") String productName, @Param("productColor") String productColor, @Param("productCountry") String productCountry);

}
