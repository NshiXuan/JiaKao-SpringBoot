package com.sx.jk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sx.jk.pojo.vo.list.ProvinceVo;
import com.sx.jk.pojo.po.ExamPlace;

import java.util.List;

public interface ExamPlaceDao extends BaseMapper<ExamPlace> {
  List<ProvinceVo> listRegionExamPlaces();
}
