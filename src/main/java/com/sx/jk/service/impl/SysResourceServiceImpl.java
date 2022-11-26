package com.sx.jk.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sx.jk.common.enhance.MpLambdaQueryWrapper;
import com.sx.jk.common.enhance.MyPage;
import com.sx.jk.common.mapStruct.MapStructs;
import com.sx.jk.common.util.Constants;
import com.sx.jk.common.util.Streams;
import com.sx.jk.mapper.SysResourceDao;
import com.sx.jk.pojo.po.SysResource;
import com.sx.jk.pojo.po.SysRoleResource;
import com.sx.jk.pojo.po.SysUserRole;
import com.sx.jk.pojo.vo.PageVo;
import com.sx.jk.pojo.vo.list.SysResourceTreeVo;
import com.sx.jk.pojo.vo.list.SysResourceVo;
import com.sx.jk.pojo.vo.req.page.SysResourcePageReqVo;
import com.sx.jk.service.SysResourceService;
import com.sx.jk.service.SysRoleResourceService;
import org.aspectj.apache.bcel.generic.RET;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service("sysResourceService")
@Transactional
public class SysResourceServiceImpl extends ServiceImpl<SysResourceDao, SysResource> implements SysResourceService {
  @Autowired
  private SysRoleResourceService roleResourceService;

  @Override
  @Transactional(readOnly = true)
  public PageVo<SysResourceVo> list(SysResourcePageReqVo reqVo) {
    MpLambdaQueryWrapper<SysResource> wrapper = new MpLambdaQueryWrapper<>();
    wrapper.like(reqVo.getKeyword(), SysResource::getName);
    wrapper.orderByDesc(SysResource::getId);
    return baseMapper.selectPage(new MyPage<>(reqVo), wrapper).buildVo(MapStructs.INSTANCE::po2vo);
  }

  @Override
  @Transactional(readOnly = true)
  public List<SysResourceVo> listParents() {
    MpLambdaQueryWrapper<SysResource> wrapper = new MpLambdaQueryWrapper<>();
    wrapper.ne(SysResource::getType, Constants.SysResourceType.BTN);
    // 按照Type升序，id降序
    wrapper.orderByAsc(SysResource::getType).orderByDesc(SysResource::getId);
    return Streams.map(baseMapper.selectList(wrapper), MapStructs.INSTANCE::po2vo);
  }

  @Override
  @Transactional(readOnly = true)
  public List<SysResourceTreeVo> listTree() {
    // 里面存放的是树状结构的VO(最外层是目录类型的资源对象)
    List<SysResourceTreeVo> vos = new ArrayList<>();

    // 存放已经从po转换成功的vo
    Map<Short, SysResourceTreeVo> doneVos = new HashMap<>();

    MpLambdaQueryWrapper<SysResource> wrapper = new MpLambdaQueryWrapper<>();
    // 按照Type升序
    wrapper.orderByAsc(SysResource::getType);
    // 里面存放查出来的po
    List<SysResource> pos = baseMapper.selectList(wrapper);
    for (SysResource po : pos) {
      // po转vo
      SysResourceTreeVo vo = po2treeVo(po);

      // 将vo存放到doneVos中
      doneVos.put(vo.getId(), vo);

      Short type = po.getType();
      if (type == Constants.SysResourceType.DIR) { // 目录
        vos.add(vo);
      } else { // 菜单、按钮
        // 从doneVos中取出VO
        SysResourceTreeVo parentVo = doneVos.get(po.getParentId());
        List<SysResourceTreeVo> children = parentVo.getChildren();
        if (children == null) {
          parentVo.setChildren(children = new ArrayList<>());
        }
        children.add(vo);
      }
    }

    return vos;
  }

  @Override
  @Transactional(readOnly = true)
  public List<Short> listIds(Integer roleId) {
    if (roleId == null || roleId <= 0) return null;

    // select role_id from sys_user_role where user_id = ?
    MpLambdaQueryWrapper<SysRoleResource> wrapper = new MpLambdaQueryWrapper();
    wrapper.select(SysRoleResource::getResourceId); // 只查roleId
    wrapper.eq(SysRoleResource::getRoleId, roleId); // userId相等

    // listObjs可以拿到第一个字段的值，这里select role_id，所以拿到的是role_id
    List<Object> ids = roleResourceService.listObjs(wrapper);
    // for (Object id : ids) {
    //   System.out.println(id + "-" + id.getClass());
    // }

    // mp返回的ids为Integer类型，我们把id转成Short类型
    return Streams.map(ids, id -> ((Integer) id).shortValue());
  }

  private List<Short> listIds(List<Short> roleIds) {
    MpLambdaQueryWrapper<SysRoleResource> wrapper = new MpLambdaQueryWrapper<>();
    wrapper.select(SysRoleResource::getResourceId);
    // 只查roleId为 roleIds中的数据
    wrapper.in(SysRoleResource::getRoleId, roleIds);

    List<Object> ids = roleResourceService.listObjs(wrapper);
    // HashSet去重
    return Streams.map(new HashSet<>(ids), id -> ((Integer) id).shortValue());
  }

  @Override
  @Transactional(readOnly = true)
  public List<SysResource> listByRoleIds(List<Short> roleIds) {
    MpLambdaQueryWrapper<SysResource> wrapper = new MpLambdaQueryWrapper<>();
    wrapper.in(SysResource::getId, listIds(roleIds));
    return baseMapper.selectList(wrapper);
  }

  private SysResourceTreeVo po2treeVo(SysResource po) {
    SysResourceTreeVo treeVo = new SysResourceTreeVo();
    treeVo.setId(po.getId());
    treeVo.setTitle(po.getName());
    return treeVo;
  }
}

