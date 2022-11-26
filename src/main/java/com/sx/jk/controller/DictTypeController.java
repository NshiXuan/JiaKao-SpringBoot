package com.sx.jk.controller;


import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.jk.common.mapStruct.MapStructs;
import com.sx.jk.common.util.Constants;
import com.sx.jk.common.util.JsonVos;
import com.sx.jk.common.util.Streams;
import com.sx.jk.pojo.po.DictItem;
import com.sx.jk.pojo.po.DictType;
import com.sx.jk.pojo.vo.DataJsonVo;
import com.sx.jk.pojo.vo.JsonVo;
import com.sx.jk.pojo.vo.PageJsonVo;
import com.sx.jk.pojo.vo.list.DictTypeVo;
import com.sx.jk.pojo.vo.req.page.DictTypePageReqVo;
import com.sx.jk.pojo.vo.req.save.DictItemReqVo;
import com.sx.jk.pojo.vo.req.save.DictTypeReqVo;
import com.sx.jk.service.DictTypeService;
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
@RequestMapping("/dictTypes")
@Api(tags = "数据字典类型")
public class DictTypeController extends BaseController<DictType, DictTypeReqVo> {
  @Autowired
  private DictTypeService service;

  @GetMapping
  @ApiOperation("分页查询")
  @RequiresPermissions(Constants.Permisson.DICT_TYPE_LIST)
  public PageJsonVo<DictTypeVo> list(DictTypePageReqVo query) {
    return JsonVos.ok(service.list(query));
  }

  @GetMapping("/list")
  @ApiOperation("查询所有")
  @RequiresPermissions(Constants.Permisson.DICT_TYPE_LIST)
  public DataJsonVo<List<DictTypeVo>> list() {
    List<DictTypeVo> vos = Streams.map(service.list(), MapStructs.INSTANCE::po2vo);
    return JsonVos.ok(vos);
  }

  @Override
  // 有下面两个权限中的一个都可以访问
  @RequiresPermissions(value = {
          Constants.Permisson.DICT_TYPE_ADD,
          Constants.Permisson.DICT_TYPE_UPDATE
  }, logical = Logical.OR)
  public JsonVo save(DictTypeReqVo dictTypeReqVo) {
    return super.save(dictTypeReqVo);
  }

  @Override
  @RequiresPermissions(Constants.Permisson.DICT_TYPE_REMOVE)
  public JsonVo remove(String id) {
    return super.remove(id);
  }

  @Override
  protected IService<DictType> getService() {
    return service;
  }

  @Override
  protected Function<DictTypeReqVo, DictType> getFunction() {
    return MapStructs.INSTANCE::reqVo2po;
  }
}

