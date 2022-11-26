package com.sx.jk.controller;


import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.jk.common.mapStruct.MapStructs;
import com.sx.jk.common.util.Constants;
import com.sx.jk.common.util.JsonVos;
import com.sx.jk.common.util.Streams;
import com.sx.jk.pojo.po.SysRole;
import com.sx.jk.pojo.result.CodeMsg;
import com.sx.jk.pojo.vo.DataJsonVo;
import com.sx.jk.pojo.vo.JsonVo;
import com.sx.jk.pojo.vo.PageJsonVo;
import com.sx.jk.pojo.vo.list.SysRoleVo;
import com.sx.jk.pojo.vo.req.page.SysRolePageReqVo;
import com.sx.jk.pojo.vo.req.save.SysRoleReqVo;
import com.sx.jk.service.SysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.function.Function;

@RestController
@RequestMapping("/sysRoles")
@Api(tags = "角色")
public class SysRoleController extends BaseController<SysRole, SysRoleReqVo> {
  @Autowired
  private SysRoleService service;

  @GetMapping("/ids")
  @ApiOperation("根据用户id获取角色id")
  @RequiresPermissions(Constants.Permisson.SYS_ROLE_LIST)
  public DataJsonVo<List<Short>> ids(Integer userId){
    return JsonVos.ok(service.listIds(userId));
  }

  @GetMapping("list")
  @ApiOperation("查询所有")
  @RequiresPermissions(Constants.Permisson.SYS_ROLE_LIST)
  public DataJsonVo<List<SysRoleVo>> list() {
    List<SysRoleVo> vos = Streams.map(service.list(), MapStructs.INSTANCE::po2vo);
    return JsonVos.ok(vos);
  }

  @GetMapping
  @ApiOperation("分页查询")
  @RequiresPermissions(Constants.Permisson.SYS_ROLE_LIST)
  public PageJsonVo<SysRoleVo> list(SysRolePageReqVo reqVo) {
    return JsonVos.ok(service.list(reqVo));
  }

  @Override
  // 有下面两个权限中的一个都可以访问
  @RequiresPermissions(value = {
          Constants.Permisson.SYS_ROLE_ADD,
          Constants.Permisson.SYS_ROLE_UPDATE
  },logical = Logical.OR)
  public JsonVo save( SysRoleReqVo reqVo) {
    if (service.saveOrUpdate(reqVo)) {
      return JsonVos.ok(CodeMsg.SAVE_OK);
    } else {
      return JsonVos.raise(CodeMsg.SAVE_ERROR);
    }
  }

  @Override
  @RequiresPermissions(Constants.Permisson.SYS_ROLE_REMOVE)
  public JsonVo remove(String id) {
    return super.remove(id);
  }

  @Override
  protected IService<SysRole> getService() {
    return service;
  }

  @Override
  protected Function<SysRoleReqVo, SysRole> getFunction() {
    return MapStructs.INSTANCE::reqVo2po;
  }
}

