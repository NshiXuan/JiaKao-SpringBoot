package com.sx.jk.controller;


import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.jk.common.mapStruct.MapStructs;
import com.sx.jk.common.util.Constants;
import com.sx.jk.common.util.JsonVos;
import com.sx.jk.pojo.po.DictItem;
import com.sx.jk.pojo.po.ExamPlaceCourse;
import com.sx.jk.pojo.result.CodeMsg;
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
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
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
    if (service.saveOrUpdate(examPlaceCourseReqVo)) {
      return JsonVos.ok(CodeMsg.SAVE_OK);
    } else {
      return JsonVos.raise(CodeMsg.SAVE_ERROR);
    }
  }

  @Override
  @RequiresPermissions(Constants.Permisson.EXAM_PLACE_COURSE_REMOVE)
  public JsonVo remove(String id) {
    System.out.println(id);
    List<String> idStrs = Arrays.asList(id.split(","));
    System.out.println(idStrs);
    for (String idStr : idStrs) {
      System.out.println(idStr+"idStr");
    }

    if (CollectionUtils.isEmpty(idStrs)) {
      return JsonVos.raise(CodeMsg.REMOVE_ERROR);
    }

    boolean ret = true;
    for (String idStr : idStrs) {
      if (!service.removeById(idStr)) {
        ret = false;
      }
    }

    return ret ? JsonVos.ok(CodeMsg.REMOVE_OK) : JsonVos.raise(CodeMsg.REMOVE_ERROR);
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

