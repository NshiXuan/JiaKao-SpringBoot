package com.sx.jk.controller;


import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.jk.common.cache.Caches;
import com.sx.jk.common.mapStruct.MapStructs;
import com.sx.jk.common.shiro.TokenFilter;
import com.sx.jk.common.util.Constants;
import com.sx.jk.common.util.JsonVos;
import com.sx.jk.pojo.po.SysUser;
import com.sx.jk.pojo.result.CodeMsg;
import com.sx.jk.pojo.vo.DataJsonVo;
import com.sx.jk.pojo.vo.JsonVo;
import com.sx.jk.pojo.vo.LoginVo;
import com.sx.jk.pojo.vo.PageJsonVo;
import com.sx.jk.pojo.vo.list.SysUserVo;
import com.sx.jk.pojo.vo.req.LoginReqVo;
import com.sx.jk.pojo.vo.req.page.SysUserPageReqVo;
import com.sx.jk.pojo.vo.req.save.SysUserReqVo;
import com.sx.jk.service.SysUserService;
import com.wf.captcha.utils.CaptchaUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.function.Function;

@RestController
@RequestMapping("/sysUsers")
@Api(tags = "用户")
public class SysUserController extends BaseController<SysUser, SysUserReqVo> {
  @Autowired
  private SysUserService service;

  @GetMapping("/captcha")
  @ApiOperation("生成验证码")
  public void captcha(HttpServletRequest request, HttpServletResponse response) throws Exception {
    CaptchaUtil.out(request, response);
  }

  @PostMapping("/login")
  @ApiOperation("登录")
  public DataJsonVo<LoginVo> login(LoginReqVo reqVo, HttpServletRequest request) {
    System.out.println(reqVo.getCaptcha());
    if (CaptchaUtil.ver(reqVo.getCaptcha(), request)) {
      return JsonVos.ok(service.login(reqVo));
    }
    return JsonVos.raise(CodeMsg.WRONG_CAPTCHA);
  }

  @PostMapping("/logout")
  @ApiOperation("退出登录")
  public JsonVo logout(@RequestHeader(TokenFilter.HEADER_TOKEN) String token) {
    // 清除token
    Caches.removeToken(token);
    return JsonVos.ok();
  }

  @Override
  // 有下面两个权限中的一个都可以访问
  @RequiresPermissions(value = {
          Constants.Permisson.SYS_USER_ADD,
          Constants.Permisson.SYS_USER_UPDATE
  },logical = Logical.OR)
  public JsonVo save(SysUserReqVo reqVo) {
    if (service.saveOrUpdate(reqVo)) {
      return JsonVos.ok(CodeMsg.SAVE_OK);
    } else {
      return JsonVos.raise(CodeMsg.SAVE_ERROR);
    }
  }

  @Override
  @RequiresPermissions(Constants.Permisson.SYS_USER_REMOVE)
  public JsonVo remove(String id) {
    return super.remove(id);
  }

  @GetMapping
  @ApiOperation("分页查询")
  @RequiresPermissions(Constants.Permisson.SYS_USER_LIST)
  public PageJsonVo<SysUserVo> list(SysUserPageReqVo reqVo) {
    return JsonVos.ok(service.list(reqVo));
  }

  @Override
  protected IService<SysUser> getService() {
    return service;
  }

  @Override
  protected Function<SysUserReqVo, SysUser> getFunction() {
    return MapStructs.INSTANCE::reqVo2po;
  }
}

