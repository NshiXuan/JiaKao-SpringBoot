package com.sx.jk.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sx.jk.common.cache.Caches;
import com.sx.jk.common.enhance.MpLambdaQueryWrapper;
import com.sx.jk.common.enhance.MyPage;
import com.sx.jk.common.mapStruct.MapStructs;
import com.sx.jk.common.util.Streams;
import com.sx.jk.mapper.SysRoleDao;
import com.sx.jk.mapper.SysUserRoleDao;
import com.sx.jk.pojo.po.SysRole;
import com.sx.jk.pojo.po.SysRoleResource;
import com.sx.jk.pojo.po.SysUserRole;
import com.sx.jk.pojo.vo.PageVo;
import com.sx.jk.pojo.vo.list.SysRoleVo;
import com.sx.jk.pojo.vo.req.page.SysRolePageReqVo;
import com.sx.jk.pojo.vo.req.save.SysRoleReqVo;
import com.sx.jk.service.SysRoleResourceService;
import com.sx.jk.service.SysRoleService;
import com.sx.jk.service.SysUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service("sysRoleService")
@Transactional
public class SysRoleServiceImpl extends ServiceImpl<SysRoleDao, SysRole> implements SysRoleService {
  @Autowired
  private SysUserRoleService userRoleService;
  @Autowired
  private SysRoleResourceService roleResourceService;
  @Autowired
  private SysUserRoleDao userRoleDao;

  @Override
  @Transactional(readOnly = true)
  public PageVo<SysRoleVo> list(SysRolePageReqVo reqVo) {
    MpLambdaQueryWrapper<SysRole> wrapper = new MpLambdaQueryWrapper<>();
    wrapper.like(reqVo.getKeyword(), SysRole::getName);
    wrapper.orderByDesc(SysRole::getId);
    return baseMapper.selectPage(new MyPage<>(reqVo), wrapper).buildVo(MapStructs.INSTANCE::po2vo);
  }

  @Override
  @Transactional(readOnly = true)
  public List<Short> listIds(Integer userId) {
    if (userId == null || userId <= 0) return null;

    // select role_id from sys_user_role where user_id = ?
    MpLambdaQueryWrapper<SysUserRole> wrapper = new MpLambdaQueryWrapper();
    wrapper.select(SysUserRole::getRoleId); // ??????roleId
    wrapper.eq(SysUserRole::getUserId, userId); // userId??????

    // listObjs??????????????????????????????????????????select role_id?????????????????????role_id
    List<Object> ids = userRoleService.listObjs(wrapper);
    for (Object id : ids) {
      System.out.println(id + "-" + id.getClass());
    }

    // ???id??????Short??????
    return Streams.map(ids, id -> ((Integer) id).shortValue());
  }

  @Override
  public boolean saveOrUpdate(SysRoleReqVo reqVo) {
    // ??????PO
    SysRole po = MapStructs.INSTANCE.reqVo2po(reqVo);

    // ??????????????????
    if (!saveOrUpdate(po)) return false;

    Short id = reqVo.getId();
    if (id != null && id > 0) {
      // ??????????????????????????????????????????????????????token????????????????????????????????????
      MpLambdaQueryWrapper<SysUserRole> wrapper = new MpLambdaQueryWrapper<>();
      wrapper.select(SysUserRole::getRoleId);
      wrapper.eq(SysUserRole::getRoleId, id);
      List<Object> userIds = userRoleDao.selectObjs(wrapper);
      if (!CollectionUtils.isEmpty(userIds)) {
        for (Object userId : userIds) {
          Caches.removeToken(Caches.get(userId));
        }
      }
    }

    // ???????????????????????????????????????
    roleResourceService.removeByRoleId(reqVo.getId());

    // ??????????????????
    String resourceIdsStr = reqVo.getResourceIds();
    if (StringUtils.isEmpty(resourceIdsStr)) return true;

    String[] resourceIds = resourceIdsStr.split(",");
    // ??????SysUserRole??????
    List<SysRoleResource> roleResources = new ArrayList<>();
    Short roleId = po.getId();
    for (String resourceId : resourceIds) {
      SysRoleResource roleResource = new SysRoleResource();
      roleResource.setRoleId(roleId);
      roleResource.setResourceId(Short.parseShort(resourceId));
      roleResources.add(roleResource);
    }

    return roleResourceService.saveBatch(roleResources);
  }

  @Override
  @Transactional(readOnly = true)
  public List<SysRole> listByUserId(Integer userId) {
    if (userId == null || userId <= 0) return null;
    // ????????????id
    List<Short> ids = listIds(userId);
    if (CollectionUtils.isEmpty(ids)) return null;

    MpLambdaQueryWrapper<SysRole> wrapper = new MpLambdaQueryWrapper<>();
    wrapper.in(SysRole::getId, listIds(userId));
    return baseMapper.selectList(wrapper);
  }

  @Override
  @Transactional(readOnly = true)
  public List<SysRole> list() {
    MpLambdaQueryWrapper<SysRole> wrapper = new MpLambdaQueryWrapper<>();
    wrapper.orderByDesc(SysRole::getId);
    return super.list(wrapper);
  }
}

