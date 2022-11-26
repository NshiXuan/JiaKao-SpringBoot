package com.sx.jk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.jk.pojo.po.SysResource;
import com.sx.jk.pojo.result.CodeMsg;
import com.sx.jk.pojo.vo.PageVo;
import com.sx.jk.pojo.vo.list.SysResourceTreeVo;
import com.sx.jk.pojo.vo.list.SysResourceVo;
import com.sx.jk.pojo.vo.req.page.SysResourcePageReqVo;

import java.util.List;

public interface SysResourceService extends IService<SysResource> {

  PageVo<SysResourceVo> list(SysResourcePageReqVo reqVo);

  List<SysResourceVo> listParents();

  List<SysResourceTreeVo> listTree();

  List<Short> listIds(Integer roleId);

  List<SysResource> listByRoleIds(List<Short> roleIds);
}

