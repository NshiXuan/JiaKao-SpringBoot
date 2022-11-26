package com.sx.jk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sx.jk.pojo.vo.list.ProvinceVo;
import com.sx.jk.pojo.po.PlateRegion;

import java.util.List;

public interface PlateRegionDao extends BaseMapper<PlateRegion> {
  List<ProvinceVo> selectRegions();
}
