package com.sx.jk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.jk.pojo.po.SysUser;
import com.sx.jk.pojo.result.CodeMsg;
import com.sx.jk.pojo.vo.DataJsonVo;
import com.sx.jk.pojo.vo.LoginVo;
import com.sx.jk.pojo.vo.PageVo;
import com.sx.jk.pojo.vo.list.SysUserVo;
import com.sx.jk.pojo.vo.req.LoginReqVo;
import com.sx.jk.pojo.vo.req.page.SysUserPageReqVo;
import com.sx.jk.pojo.vo.req.save.SysUserReqVo;

public interface SysUserService extends IService<SysUser> {
  PageVo<SysUserVo>  list(SysUserPageReqVo reqVo);

  boolean saveOrUpdate(SysUserReqVo reqVo);

  LoginVo login(LoginReqVo reqVo);
}

