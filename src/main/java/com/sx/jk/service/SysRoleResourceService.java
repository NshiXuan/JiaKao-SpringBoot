package com.sx.jk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.jk.pojo.po.SysRoleResource;

public interface SysRoleResourceService extends IService<SysRoleResource> {

  boolean removeByRoleId(Short roleId);
}

