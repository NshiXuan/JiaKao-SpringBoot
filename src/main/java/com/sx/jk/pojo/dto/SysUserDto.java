package com.sx.jk.pojo.dto;

import com.sx.jk.pojo.po.SysResource;
import com.sx.jk.pojo.po.SysRole;
import com.sx.jk.pojo.po.SysUser;
import lombok.Data;

import java.util.List;

@Data
public class SysUserDto {
  private SysUser user;
  private List<SysRole> roles;
  private List<SysResource> resources;
}
