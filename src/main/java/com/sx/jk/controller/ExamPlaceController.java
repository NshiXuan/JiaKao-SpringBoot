package com.sx.jk.controller;


import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.jk.common.mapStruct.MapStructs;
import com.sx.jk.common.util.Constants;
import com.sx.jk.common.util.JsonVos;
import com.sx.jk.pojo.po.DictItem;
import com.sx.jk.pojo.po.ExamPlace;
import com.sx.jk.pojo.vo.DataJsonVo;
import com.sx.jk.pojo.vo.JsonVo;
import com.sx.jk.pojo.vo.PageJsonVo;
import com.sx.jk.pojo.vo.list.ExamPlaceVo;
import com.sx.jk.pojo.vo.list.ProvinceVo;
import com.sx.jk.pojo.vo.req.page.ExamPlacePageReqVo;
import com.sx.jk.pojo.vo.req.save.DictItemReqVo;
import com.sx.jk.pojo.vo.req.save.ExamPlaceReqVo;
import com.sx.jk.service.ExamPlaceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.function.Function;

@RestController
@RequestMapping("/examPlaces")
@Api(tags = "考场")
public class ExamPlaceController extends BaseController<ExamPlace, ExamPlaceReqVo> {
  @Autowired
  private ExamPlaceService service;

  @GetMapping
  @ApiOperation("分页查询")
  @RequiresPermissions(Constants.Permisson.EXAM_PLACE_LIST)
  public PageJsonVo<ExamPlaceVo> list(ExamPlacePageReqVo query) {
    return JsonVos.ok(service.list(query));

  }

  @GetMapping("/regionExamPlaces")
  @ApiOperation("加载所有区域下面的考场信息")
  @RequiresPermissions(Constants.Permisson.EXAM_PLACE_LIST)
  public DataJsonVo<List<ProvinceVo>> listRegionExamPlaces() {
    return JsonVos.ok(service.listRegionExamPlaces());
  }

  @Override
  // 有下面两个权限中的一个都可以访问
  @RequiresPermissions(value = {
          Constants.Permisson.EXAM_PLACE_ADD,
          Constants.Permisson.EXAM_PLACE_UPDATE
  }, logical = Logical.OR)
  public JsonVo save(ExamPlaceReqVo examPlaceReqVo) {
    return super.save(examPlaceReqVo);
  }

  @Override
  @RequiresPermissions(Constants.Permisson.EXAM_PLACE_REMOVE)
  public JsonVo remove(String id) {
    return super.remove(id);
  }

  @Override
  protected IService<ExamPlace> getService() {
    return service;
  }

  @Override
  protected Function<ExamPlaceReqVo, ExamPlace> getFunction() {
    return MapStructs.INSTANCE::reqVo2po;
  }
}

