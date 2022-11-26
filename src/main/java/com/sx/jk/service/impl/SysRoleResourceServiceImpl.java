package com.sx.jk.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sx.jk.common.enhance.MpLambdaQueryWrapper;
import com.sx.jk.mapper.SysRoleResourceDao;
import com.sx.jk.pojo.po.SysRoleResource;
import com.sx.jk.service.SysRoleResourceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("sysRoleResourceService")
@Transactional
public class SysRoleResourceServiceImpl extends ServiceImpl<SysRoleResourceDao, SysRoleResource> implements SysRoleResourceService {

  @Override
  public boolean removeByRoleId(Short roleId) {
    if (roleId == null || roleId <= 0) return false;
    MpLambdaQueryWrapper<SysRoleResource> wrapper = new MpLambdaQueryWrapper<>();
    wrapper.eq(SysRoleResource::getRoleId, roleId);
    return baseMapper.delete(wrapper) > 0;
  }
}

