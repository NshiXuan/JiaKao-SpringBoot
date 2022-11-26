package com.sx.jk.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sx.jk.common.enhance.MyPage;
import com.sx.jk.common.enhance.MpLambdaQueryWrapper;
import com.sx.jk.common.mapStruct.MapStructs;
import com.sx.jk.common.util.Streams;
import com.sx.jk.mapper.PlateRegionDao;
import com.sx.jk.pojo.vo.PageVo;
import com.sx.jk.pojo.vo.list.PlateRegionVo;
import com.sx.jk.pojo.vo.list.ProvinceVo;
import com.sx.jk.pojo.po.PlateRegion;
import com.sx.jk.pojo.vo.req.page.CityPageReqVo;
import com.sx.jk.pojo.vo.req.page.ProvincePageReqVo;
import com.sx.jk.service.PlateRegionService;
import me.majiajie.tinypinyin.Pinyin;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("plateRegionService")
@Transactional
public class PlateRegionServiceImpl extends ServiceImpl<PlateRegionDao, PlateRegion> implements PlateRegionService {
  @Override
  public boolean save(PlateRegion entity) {
    processPinyin(entity);
    return super.save(entity);
  }

  @Override
  public boolean updateById(PlateRegion entity) {
    processPinyin(entity);
    return super.updateById(entity);
  }

  private void processPinyin(PlateRegion region) {
    String name = region.getName();
    if (name == null) return;
    region.setPinyin(Pinyin.toPinyin(name, "_"));
  }

  @Override
  public List<ProvinceVo> listRegions() {
    return baseMapper.selectRegions();
  }

  @Override
  @Transactional(readOnly = true)
  public PageVo<PlateRegionVo> listProvinces(ProvincePageReqVo query) {
    MpLambdaQueryWrapper<PlateRegion> wrapper = new MpLambdaQueryWrapper<>();
    wrapper.like(query.getKeyword(), PlateRegion::getName, PlateRegion::getPlate, PlateRegion::getPinyin);
    // 所有省份
    wrapper.eq(PlateRegion::getParentId, 0);
    wrapper.orderByDesc(PlateRegion::getId);
    return baseMapper.
            selectPage(new MyPage<>(query), wrapper).
            buildVo(MapStructs.INSTANCE::po2vo);
  }

  @Override
  @Transactional(readOnly = true)
  public PageVo<PlateRegionVo> listCities(CityPageReqVo query) {
    MpLambdaQueryWrapper<PlateRegion> wrapper = new MpLambdaQueryWrapper<>();
    wrapper.like(query.getKeyword(),
            PlateRegion::getName,
            PlateRegion::getPlate,
            PlateRegion::getPinyin);
    Integer provinceId = query.getParentId();
    if (provinceId != null && provinceId > 0) { // provinceId下面的所有城市
      wrapper.eq(PlateRegion::getParentId, provinceId);
    } else { // 所有城市
      wrapper.ne(PlateRegion::getParentId, 0);
    }
    wrapper.orderByDesc(PlateRegion::getId);
    return baseMapper.
            selectPage(new MyPage<>(query), wrapper).
            buildVo(MapStructs.INSTANCE::po2vo);
  }

  @Override
  public List<PlateRegionVo> listProvinces() {
    MpLambdaQueryWrapper<PlateRegion> wrapper = new MpLambdaQueryWrapper<>();
    wrapper.eq(PlateRegion::getParentId, 0);
    // 按照拼音升序
    wrapper.orderByAsc(PlateRegion::getPinyin);
    return Streams.map(baseMapper.selectList(wrapper), MapStructs.INSTANCE::po2vo);
  }
}

