package com.sx.jk.controller;


import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.jk.common.mapStruct.MapStructs;
import com.sx.jk.common.util.Constants;
import com.sx.jk.common.util.JsonVos;
import com.sx.jk.pojo.po.DictItem;
import com.sx.jk.pojo.vo.JsonVo;
import com.sx.jk.pojo.vo.PageJsonVo;
import com.sx.jk.pojo.vo.list.DictItemVo;
import com.sx.jk.pojo.vo.req.page.DictItemPageReqVo;
import com.sx.jk.pojo.vo.req.save.DictItemReqVo;
import com.sx.jk.service.DictItemService;
import io.swagger.annotations.*;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.function.Function;

@RestController
@RequestMapping("/dictItems")
@Api(tags = "数据字典条目")
public class DictItemController extends BaseController<DictItem, DictItemReqVo> {
  @Autowired
  private DictItemService service;

  @GetMapping
  @ApiOperation(value = "分页查询")
  @RequiresPermissions(Constants.Permisson.DICT_ITEM_LIST)
  public PageJsonVo<DictItemVo> list(DictItemPageReqVo query) {
    return JsonVos.ok(service.list(query));

  }

  @Override
  // 有下面两个权限中的一个都可以访问
  @RequiresPermissions(value = {
          Constants.Permisson.DICT_ITEM_ADD,
          Constants.Permisson.DICT_ITEM_UPDATE
  }, logical = Logical.OR)
  public JsonVo save(DictItemReqVo reqVo) {
    return super.save(reqVo);
  }

  @Override
  @RequiresPermissions(Constants.Permisson.DICT_ITEM_REMOVE)
  public JsonVo remove(String id) {
    return super.remove(id);
  }

  @Override
  protected IService<DictItem> getService() {
    return service;
  }

  @Override
  protected Function<DictItemReqVo, DictItem> getFunction() {
    return MapStructs.INSTANCE::reqVo2po;
  }
}

