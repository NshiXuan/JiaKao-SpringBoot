package com.sx.jk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sx.jk.common.enhance.MpQueryWrapper;
import com.sx.jk.common.enhance.MyPage;
import com.sx.jk.mapper.ExamPlaceCourseDao;
import com.sx.jk.pojo.po.ExamPlaceCourse;
import com.sx.jk.pojo.vo.PageVo;
import com.sx.jk.pojo.vo.list.ExamPlaceCourseVo;
import com.sx.jk.pojo.vo.req.page.ExamPlaceCoursePageReqVo;
import com.sx.jk.service.ExamPlaceCourseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service("examPlaceCourseService")
@Transactional
public class ExamPlaceCourseServiceImpl extends ServiceImpl<ExamPlaceCourseDao, ExamPlaceCourse> implements ExamPlaceCourseService {

  @Override
  @Transactional(readOnly = true)
  public PageVo<ExamPlaceCourseVo> list(ExamPlaceCoursePageReqVo query) {
    // 查询条件
    MpQueryWrapper<ExamPlaceCourseVo> wrapper = new MpQueryWrapper<>();
    Integer placeId = query.getPlaceId();
    Integer provinceId = query.getProvinceId();
    Integer cityId = query.getCityId();
    Short type = query.getType();

    // 类型
    if (type != null && type >= 0) {
      wrapper.eq("c.type", type);
    }

    // 考场 - 城市 - 省份
    if (placeId != null && placeId > 0) {
      wrapper.eq("c.place_id", placeId);
    } else if (cityId != null && cityId > 0) {
      wrapper.eq("p.city_id", cityId);
    } else if (provinceId != null && provinceId > 0) {
      wrapper.eq("p.province_id", provinceId);
    }

    // 关键字
    // wrapper.like(query.getKeyword(), ExamPlaceCourseVo::getName, ExamPlaceCourseVo::getIntro);
    String keyword = query.getKeyword();
    wrapper.like(query.getKeyword(), "c.name", "c.intro");
    // if (!StringUtils.isEmpty(keyword)) {
    //   wrapper.nested(w -> {
    //     w.like("c.name", keyword).or().like("c.intro", keyword);
    //   });
    // }

    // 查询
    return baseMapper.
            selectPageVos(new MyPage<>(query), wrapper).
            buildVo();
  }


  // @Override
  // @Transactional(readOnly = true)
  // public PageVo<ExamPlaceCourseVo> list(ExamPlaceCoursePageReqVo query) {
  //   // 查询条件
  //   MyQueryWrapper<ExamPlaceCourseVo> wrapper = new MyQueryWrapper<>();
  //   Integer placeId = query.getPlaceId();
  //   Integer provinceId = query.getProvinceId();
  //   Integer cityId = query.getCityId();
  //   Short type = query.getType();
  //
  //   // 类型
  //   if (type != null && type >= 0) {
  //     wrapper.eq(ExamPlaceCourseVo::getType, type);
  //   }
  //
  //   // 考场 - 城市 - 省份
  //   if (placeId != null && placeId > 0) {
  //     wrapper.eq(ExamPlaceCourseVo::getPlaceId, placeId);
  //   } else if (cityId != null && cityId > 0) {
  //     wrapper.eq(ExamPlaceCourseVo::getCityId, cityId);
  //   } else if (provinceId != null && provinceId > 0) {
  //     wrapper.eq(ExamPlaceCourseVo::getProvinceId, provinceId);
  //   }
  //
  //   // 关键字
  //   wrapper.like(query.getKeyword(), ExamPlaceCourseVo::getName, ExamPlaceCourseVo::getIntro);
  //
  //   // 查询
  //   return baseMapper.
  //           selectPageVos(new MyPage<>(query), wrapper).
  //           buildVo();
  // }
}

