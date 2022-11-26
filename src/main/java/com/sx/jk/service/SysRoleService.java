package com.sx.jk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.jk.pojo.po.SysRole;
import com.sx.jk.pojo.vo.PageJsonVo;
import com.sx.jk.pojo.vo.PageVo;
import com.sx.jk.pojo.vo.list.SysRoleVo;
import com.sx.jk.pojo.vo.req.page.SysRolePageReqVo;
import com.sx.jk.pojo.vo.req.save.SysRoleReqVo;

import java.util.List;

public interface SysRoleService extends IService<SysRole> {

  PageVo<SysRoleVo> list(SysRolePageReqVo reqVo);

  List<Short> listIds(Integer userId);

  boolean saveOrUpdate(SysRoleReqVo reqVo);

  List<SysRole> listByUserId(Integer userId);
}

