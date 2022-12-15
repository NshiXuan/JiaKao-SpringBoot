package com.sx.jk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sx.jk.common.enhance.MpQueryWrapper;
import com.sx.jk.common.enhance.MyPage;
import com.sx.jk.common.mapStruct.MapStructs;
import com.sx.jk.common.util.JsonVos;
import com.sx.jk.common.util.Uploads;
import com.sx.jk.mapper.ExamPlaceCourseDao;
import com.sx.jk.pojo.po.ExamPlaceCourse;
import com.sx.jk.pojo.result.CodeMsg;
import com.sx.jk.pojo.vo.PageVo;
import com.sx.jk.pojo.vo.list.ExamPlaceCourseVo;
import com.sx.jk.pojo.vo.req.page.ExamPlaceCoursePageReqVo;
import com.sx.jk.pojo.vo.req.save.ExamPlaceCourseReqVo;
import com.sx.jk.service.ExamPlaceCourseService;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

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

  @Override
  public boolean saveOrUpdate(ExamPlaceCourseReqVo reqVo) {
    try {
      ExamPlaceCourse po = MapStructs.INSTANCE.reqVo2po(reqVo);

      // 上传图片
      String filepath = Uploads.uploadImage(reqVo.getCoverFile());

      // 有新的图片上传成功，删除旧封面，添加新图片
      if (filepath != null) {
        // 设置新的封面
        po.setCover(filepath);
      }

      // 保存po
      boolean ret = saveOrUpdate(po);
      if (ret && filepath != null) {
        Uploads.deleteFile(reqVo.getCover());
      }

      return ret;
    } catch (Exception e) {
      return JsonVos.raise(CodeMsg.UPLOAD_IMG_ERROR);
    }
  }

  @Override
  public boolean removeById(Serializable id) {
    ExamPlaceCourse course = getById(id);
    try {
      if (super.removeById(id)) {
        Uploads.deleteFile(course.getCover());
      }
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  // @Override
  // public boolean removeByIds(Collection<? extends Serializable> idList) {
  //   if (CollectionUtils.isEmpty(idList)) return false;
  //
  //   // 如果数据库删除失败，直接返回，不删除图片
  //   boolean result = super.removeByIds(idList);
  //   if (!result) return false;
  //
  //   // 批量查出要删除的对象，再把对应的图片删除
  //   List<ExamPlaceCourse> courses = listByIds(idList);
  //   for (ExamPlaceCourse course : courses) {
  //     System.out.println(course.getCover() + "course");
  //     try {
  //       Uploads.deleteFile(course.getCover());
  //     } catch (Exception e) {
  //       return JsonVos.raise(CodeMsg.REMOVE_ERROR);
  //     }
  //   }
  //
  //   return true;
  // }

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

