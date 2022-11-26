package com.sx.jk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.jk.pojo.po.DictType;
import com.sx.jk.pojo.vo.PageVo;
import com.sx.jk.pojo.vo.list.DictTypeVo;
import com.sx.jk.pojo.vo.req.page.DictTypePageReqVo;

public interface DictTypeService extends IService<DictType> {
  PageVo<DictTypeVo> list(DictTypePageReqVo query);
}

