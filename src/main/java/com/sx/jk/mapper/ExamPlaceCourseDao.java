package com.sx.jk.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.sx.jk.common.enhance.MyPage;
import com.sx.jk.pojo.po.ExamPlaceCourse;
import com.sx.jk.pojo.vo.list.ExamPlaceCourseVo;
import org.apache.ibatis.annotations.Param;

public interface ExamPlaceCourseDao extends BaseMapper<ExamPlaceCourse> {

  // MyPage<ExamPlaceCourseVo> selectPageVos(MyPage<ExamPlaceCourseVo> page,
  //                                        @Param(Constants.WRAPPER) MyQueryWrapper<ExamPlaceCourseVo> wrapper);

  MyPage<ExamPlaceCourseVo> selectPageVos(MyPage<ExamPlaceCourseVo> page,
                                          @Param(Constants.WRAPPER) Wrapper<ExamPlaceCourseVo> wrapper);
}
