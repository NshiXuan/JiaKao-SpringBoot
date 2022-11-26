package com.sx.jk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.jk.pojo.vo.PageVo;
import com.sx.jk.pojo.vo.list.ExamPlaceVo;
import com.sx.jk.pojo.vo.list.ProvinceVo;
import com.sx.jk.pojo.po.ExamPlace;
import com.sx.jk.pojo.vo.req.page.ExamPlacePageReqVo;

import java.util.List;

public interface ExamPlaceService extends IService<ExamPlace> {
  PageVo<ExamPlaceVo> list(ExamPlacePageReqVo query);
  List<ProvinceVo> listRegionExamPlaces();
}

