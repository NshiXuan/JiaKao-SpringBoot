package com.sx.jk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.jk.pojo.vo.PageVo;
import com.sx.jk.pojo.vo.list.PlateRegionVo;
import com.sx.jk.pojo.vo.list.ProvinceVo;
import com.sx.jk.pojo.po.PlateRegion;
import com.sx.jk.pojo.vo.req.page.CityPageReqVo;
import com.sx.jk.pojo.vo.req.page.ProvincePageReqVo;

import java.util.List;

public interface  PlateRegionService extends IService<PlateRegion> {
  List<ProvinceVo> listRegions();
  PageVo<PlateRegionVo> listProvinces(ProvincePageReqVo query);
  PageVo<PlateRegionVo> listCities(CityPageReqVo query);
  List<PlateRegionVo> listProvinces();
}

