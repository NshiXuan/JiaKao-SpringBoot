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
    // ??????PO
    SysUser po = MapStructs.INSTANCE.reqVo2po(reqVo);

    // ??????????????????
    if (!saveOrUpdate(po)) return false;

    Integer id = reqVo.getId();
    if (id != null && id > 0) { // ??????
      // ????????????????????????????????????????????????token????????????????????????????????????
      Caches.removeToken(Caches.get(po.getId()));
      Caches.remove(po.getId());

      // ???????????????????????????????????????
      userRoleService.removeByUserId(reqVo.getId());
    }

    // ??????????????????
    String roleIdsStr = reqVo.getRoleIds();
    if (StringUtils.isEmpty(roleIdsStr)) return true;

    String[] roleIds = roleIdsStr.split(",");

    // ??????SysUserRole??????
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
      // ??????????????????
      return JsonVos.raise(CodeMsg.WRONG_USERNAME);
    }

    // ???????????????
    if (!po.getPassword().equals(reqVo.getPassword())) {
      return JsonVos.raise(CodeMsg.WRONG_PASSWORD);
    }

    // ????????????
    if (po.getStatus() == Constants.SysUserStatus.LOCKED) {
      return JsonVos.raise(CodeMsg.USER_LOCKED);
    }

    // ??????????????????
    po.setLoginTime(new Date());
    baseMapper.updateById(po);

    // ??????Token?????????token?????????
    String token = UUID.randomUUID().toString();

    SysUserDto dto = new SysUserDto();
    dto.setUser(po);

    // ????????????id??????????????????
    List<SysRole> roles = roleService.listByUserId(po.getId());

    // ????????????id??????????????????
    if (!CollectionUtils.isEmpty(roles)) {
      dto.setRoles(roles);
      List<Short> roleIds = Streams.map(roles, SysRole::getId);
      List<SysResource> resources = resourceService.listByRoleIds(roleIds);
      dto.setResources(resources);
    }

    // ??????token????????????
    Caches.putToken(token, dto);

    LoginVo vo = MapStructs.INSTANCE.po2loginVo(po);
    vo.setToken(token);

    return vo;
  }
}

