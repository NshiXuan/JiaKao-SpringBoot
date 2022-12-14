package com.sx.jk.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sx.jk.common.cache.Caches;
import com.sx.jk.common.enhance.MpLambdaQueryWrapper;
import com.sx.jk.common.enhance.MyPage;
import com.sx.jk.common.mapStruct.MapStructs;
import com.sx.jk.common.util.Constants;
import com.sx.jk.common.util.JsonVos;
import com.sx.jk.common.util.Streams;
import com.sx.jk.mapper.SysUserDao;
import com.sx.jk.pojo.dto.SysUserDto;
import com.sx.jk.pojo.po.SysResource;
import com.sx.jk.pojo.po.SysRole;
import com.sx.jk.pojo.po.SysUser;
import com.sx.jk.pojo.po.SysUserRole;
import com.sx.jk.pojo.result.CodeMsg;
import com.sx.jk.pojo.vo.LoginVo;
import com.sx.jk.pojo.vo.PageVo;
import com.sx.jk.pojo.vo.list.SysUserVo;
import com.sx.jk.pojo.vo.req.LoginReqVo;
import com.sx.jk.pojo.vo.req.page.SysUserPageReqVo;
import com.sx.jk.pojo.vo.req.save.SysUserReqVo;
import com.sx.jk.service.SysResourceService;
import com.sx.jk.service.SysRoleService;
import com.sx.jk.service.SysUserRoleService;
import com.sx.jk.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service("sysUserService")
@Transactional
public class SysUserServiceImpl extends ServiceImpl<SysUserDao, SysUser> implements SysUserService {
  @Autowired
  private SysUserRoleService userRoleService;
  @Autowired
  private SysRoleService roleService;
  @Autowired
  private SysResourceService resourceService;

  @Override
  @Transactional(readOnly = true)
  public PageVo<SysUserVo> list(SysUserPageReqVo reqVo) {
    MpLambdaQueryWrapper<SysUser> wrapper = new MpLambdaQueryWrapper<>();
    wrapper.like(reqVo.getKeyword(), SysUser::getNickname, SysUser::getUsername);
    wrapper.orderByDesc(SysUser::getId);
    return baseMapper.selectPage(new MyPage<>(reqVo), wrapper).buildVo(MapStructs.INSTANCE::po2vo);
  }

  @Override
  public boolean saveOrUpdate(SysUserReqVo reqVo) {
    // 传成PO
    SysUser po = MapStructs.INSTANCE.reqVo2po(reqVo);

    // 保存用户信息
    if (!saveOrUpdate(po)) return false;

    Integer id = reqVo.getId();
    if (id != null && id > 0) { // 更新
      // 将更新成功的用户从缓存中移除（让token失效，用户必须重新登录）
      Caches.removeToken(Caches.get(po.getId()));
      Caches.remove(po.getId());

      // 删除当前用户的所有角色信息
      userRoleService.removeByUserId(reqVo.getId());
    }

    // 保存角色信息
    String roleIdsStr = reqVo.getRoleIds();
    if (StringUtils.isEmpty(roleIdsStr)) return true;

    String[] roleIds = roleIdsStr.split(",");

    // 构建SysUserRole对象
    List<SysUserRole> userRoles = new ArrayList<>();
    Integer userId = po.getId();
    for (String roleId : roleIds) {
      SysUserRole userRole = new SysUserRole();
      userRole.setUserId(userId);
      userRole.setRoleId(Short.parseShort(roleId));
      userRoles.add(userRole);
    }

    return userRoleService.saveBatch(userRoles);
  }

  @Override
  public LoginVo login(LoginReqVo reqVo) {
    MpLambdaQueryWrapper<SysUser> wrapper = new MpLambdaQueryWrapper<>();
    wrapper.eq(SysUser::getUsername, reqVo.getUsername());
    SysUser po = baseMapper.selectOne(wrapper);
    if (po == null) {
      // 用户名不存在
      return JsonVos.raise(CodeMsg.WRONG_USERNAME);
    }

    // 密码不正确
    if (!po.getPassword().equals(reqVo.getPassword())) {
      return JsonVos.raise(CodeMsg.WRONG_PASSWORD);
    }

    // 账号锁定
    if (po.getStatus() == Constants.SysUserStatus.LOCKED) {
      return JsonVos.raise(CodeMsg.USER_LOCKED);
    }

    // 更新登录时间
    po.setLoginTime(new Date());
    baseMapper.updateById(po);

    // 生成Token，发送token给用户
    String token = UUID.randomUUID().toString();

    SysUserDto dto = new SysUserDto();
    dto.setUser(po);

    // 根据用户id查询所有角色
    List<SysRole> roles = roleService.listByUserId(po.getId());

    // 根据用户id查询所有资源
    if (!CollectionUtils.isEmpty(roles)) {
      dto.setRoles(roles);
      List<Short> roleIds = Streams.map(roles, SysRole::getId);
      List<SysResource> resources = resourceService.listByRoleIds(roleIds);
      dto.setResources(resources);
    }

    // 存储token到缓存中
    Caches.putToken(token, dto);

    LoginVo vo = MapStructs.INSTANCE.po2loginVo(po);
    vo.setToken(token);

    return vo;
  }
}

