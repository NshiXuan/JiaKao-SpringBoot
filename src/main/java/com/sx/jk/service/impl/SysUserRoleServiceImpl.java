package com.sx.jk.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sx.jk.common.enhance.MpLambdaQueryWrapper;
import com.sx.jk.mapper.SysUserRoleDao;
import com.sx.jk.pojo.po.SysUserRole;
import com.sx.jk.service.SysUserRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("sysUserRoleService")
@Transactional
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleDao, SysUserRole> implements SysUserRoleService {

  @Override
  public boolean removeByUserId(Integer userId) {
    if (userId == null || userId <= 0) return false;
    MpLambdaQueryWrapper<SysUserRole> wrapper = new MpLambdaQueryWrapper<>();
    wrapper.eq(SysUserRole::getUserId, userId);
    return baseMapper.delete(wrapper) > 0;
  }
}

