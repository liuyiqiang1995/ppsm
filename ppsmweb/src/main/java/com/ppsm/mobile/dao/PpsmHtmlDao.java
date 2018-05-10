package com.ppsm.mobile.dao;

import com.ppsm.mobile.entity.PpsmHtml;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @Description:
 * @Author: LiuYiQiang
 * @Date: 21:15 2018/4/26
 */
@Repository
public interface PpsmHtmlDao {

     PpsmHtml queryHtml();

     void insertHtml(String html);

     void updateHtmlById(@Param("id") int id, @Param("html") String html);
}
