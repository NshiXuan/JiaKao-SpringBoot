package com.sx.jk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.jk.pojo.po.ExamPlaceCourse;
import com.sx.jk.pojo.vo.PageVo;
import com.sx.jk.pojo.vo.list.ExamPlaceCourseVo;
import com.sx.jk.pojo.vo.req.page.ExamPlaceCoursePageReqVo;
import com.sx.jk.pojo.vo.req.save.ExamPlaceCourseReqVo;

public interface ExamPlaceCourseService extends IService<ExamPlaceCourse> {
  PageVo<ExamPlaceCourseVo> list(ExamPlaceCoursePageReqVo query);

  boolean saveOrUpdate(ExamPlaceCourseReqVo reqVo);
}

