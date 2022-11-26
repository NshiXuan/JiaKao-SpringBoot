package com.sx.jk.controller;


import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.jk.common.mapStruct.MapStructs;
import com.sx.jk.common.util.Constants;
import com.sx.jk.common.util.JsonVos;
import com.sx.jk.pojo.po.DictItem;
import com.sx.jk.pojo.po.ExamPlaceCourse;
import com.sx.jk.pojo.vo.JsonVo;
import com.sx.jk.pojo.vo.PageJsonVo;
import com.sx.jk.pojo.vo.list.ExamPlaceCourseVo;
import com.sx.jk.pojo.vo.req.page.ExamPlaceCoursePageReqVo;
import com.sx.jk.pojo.vo.req.save.DictItemReqVo;
import com.sx.jk.pojo.vo.req.save.ExamPlaceCourseReqVo;
import com.sx.jk.service.ExamPlaceCourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.function.Function;

@RestController
@RequestMapping("/examPlaceCourses")
@Api(tags = "课程")
public class ExamPlaceCourseController extends BaseController<ExamPlaceCourse, ExamPlaceCourseReqVo> {
  @Autowired
  private ExamPlaceCourseService service;

  @GetMapping
  @ApiOperation("分页查询")
  @RequiresPermissions(Constants.Permisson.EXAM_PLACE_COURSE_LIST)
  public PageJsonVo<ExamPlaceCourseVo> list(ExamPlaceCoursePageReqVo query) {
    return JsonVos.ok(service.list(query));
  }

  @Override
  // 有下面两个权限中的一个都可以访问
  @RequiresPermissions(value = {
          Constants.Permisson.EXAM_PLACE_COURSE_ADD,
          Constants.Permisson.EXAM_PLACE_COURSE_UPDATE
  }, logical = Logical.OR)
  public JsonVo save(ExamPlaceCourseReqVo examPlaceCourseReqVo) {
    return super.save(examPlaceCourseReqVo);
  }

  @Override
  @RequiresPermissions(Constants.Permisson.EXAM_PLACE_COURSE_REMOVE)
  public JsonVo remove(String id) {
    return super.remove(id);
  }

  @Override
  protected IService<ExamPlaceCourse> getService() {
    return service;
  }

  @Override
  protected Function<ExamPlaceCourseReqVo, ExamPlaceCourse> getFunction() {
    return MapStructs.INSTANCE::reqVo2po;
  }
}

