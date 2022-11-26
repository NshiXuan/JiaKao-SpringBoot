package com.sx.jk.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sx.jk.common.enhance.MyPage;
import com.sx.jk.common.enhance.MpLambdaQueryWrapper;
import com.sx.jk.common.mapStruct.MapStructs;
import com.sx.jk.mapper.ExamPlaceDao;
import com.sx.jk.pojo.vo.PageVo;
import com.sx.jk.pojo.vo.list.ExamPlaceVo;
import com.sx.jk.pojo.vo.list.ProvinceVo;
import com.sx.jk.pojo.po.ExamPlace;
import com.sx.jk.pojo.vo.req.page.ExamPlacePageReqVo;
import com.sx.jk.service.ExamPlaceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("examPlaceService")
@Transactional
public class ExamPlaceServiceImpl extends ServiceImpl<ExamPlaceDao, ExamPlace> implements ExamPlaceService {
  @Override
  @Transactional(readOnly = true)
  public PageVo<ExamPlaceVo> list(ExamPlacePageReqVo query) {
    // 查询条件
    MpLambdaQueryWrapper<ExamPlace> wrapper = new MpLambdaQueryWrapper<>();
    wrapper.like(query.getKeyword(), ExamPlace::getName, ExamPlace::getAddress);
    Integer provinceId = query.getProvinceId();
    Integer cityId = query.getCityId();
    if (cityId != null && cityId > 0) {
      wrapper.eq(ExamPlace::getCityId, cityId);
    }else if (provinceId != null && provinceId > 0) {
      wrapper.eq(ExamPlace::getProvinceId, provinceId);
    }

    // 排序
    wrapper.orderByDesc(ExamPlace::getId);

    // 查询
    return baseMapper.
            selectPage(new MyPage<>(query), wrapper).
            buildVo(MapStructs.INSTANCE::po2vo);
  }

  @Override
  @Transactional(readOnly = true)
  public List<ProvinceVo> listRegionExamPlaces() {
    return baseMapper.listRegionExamPlaces();
  }
}

