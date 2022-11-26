package com.sx.jk.controller;


import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.jk.common.mapStruct.MapStructs;
import com.sx.jk.common.util.Constants;
import com.sx.jk.common.util.JsonVos;
import com.sx.jk.common.util.Streams;
import com.sx.jk.pojo.po.SysResource;
import com.sx.jk.pojo.vo.DataJsonVo;
import com.sx.jk.pojo.vo.JsonVo;
import com.sx.jk.pojo.vo.PageJsonVo;
import com.sx.jk.pojo.vo.list.SysResourceTreeVo;
import com.sx.jk.pojo.vo.list.SysResourceVo;
import com.sx.jk.pojo.vo.req.page.SysResourcePageReqVo;
import com.sx.jk.pojo.vo.req.save.SysResourceReqVo;
import com.sx.jk.service.SysResourceService;
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
@RequestMapping("/sysResources")
@Api(tags = "资源")
public class SysResourceController extends BaseController<SysResource, SysResourceReqVo> {
  @Autowired
  private SysResourceService service;

  @GetMapping("/ids")
  @ApiOperation("根据角色id获取资源id")
  @RequiresPermissions(Constants.Permisson.SYS_RESOURCE_LIST)
  public DataJsonVo<List<Short>> ids(Integer roleId) {
    return JsonVos.ok(service.listIds(roleId));
  }

  @GetMapping
  @ApiOperation("分页查询")
  @RequiresPermissions(Constants.Permisson.SYS_RESOURCE_LIST)
  public PageJsonVo<SysResourceVo> list(SysResourcePageReqVo reqVo) {
    return JsonVos.ok(service.list(reqVo));
  }

  @GetMapping("/list")
  @ApiOperation("查询所有")
  @RequiresPermissions(Constants.Permisson.SYS_RESOURCE_LIST)
  public DataJsonVo<List<SysResourceVo>> list() {
    return JsonVos.ok(Streams.map(service.list(), MapStructs.INSTANCE::po2vo));
  }

  @GetMapping("/listParents")
  @ApiOperation("查询所有父资源（目录、菜单）")
  @RequiresPermissions(Constants.Permisson.SYS_RESOURCE_LIST)
  public DataJsonVo<List<SysResourceVo>> listParents() {
    return JsonVos.ok(service.listParents());
  }

  @GetMapping("/listTree")
  @ApiOperation("查询所有（树状结构）")
  @RequiresPermissions(Constants.Permisson.SYS_RESOURCE_LIST)
  public DataJsonVo<List<SysResourceTreeVo>> listTree() {
    return JsonVos.ok(service.listTree());
  }

  @Override
  // 有下面两个权限中的一个都可以访问
  @RequiresPermissions(value = {
          Constants.Permisson.SYS_RESOURCE_ADD,
          Constants.Permisson.SYS_RESOURCE_UPDATE
  }, logical = Logical.OR)
  public JsonVo save(@Valid SysResourceReqVo sysResourceReqVo) {
    return super.save(sysResourceReqVo);
  }

  @Override
  @RequiresPermissions(Constants.Permisson.SYS_RESOURCE_REMOVE)
  public JsonVo remove(String id) {
    return super.remove(id);
  }

  @Override
  protected IService<SysResource> getService() {
    return service;
  }

  @Override
  protected Function<SysResourceReqVo, SysResource> getFunction() {
    return MapStructs.INSTANCE::reqVo2po;
  }
}

