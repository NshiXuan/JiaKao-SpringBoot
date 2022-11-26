package com.sx.jk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.jk.pojo.po.DictItem;
import com.sx.jk.pojo.vo.PageVo;
import com.sx.jk.pojo.vo.list.DictItemVo;
import com.sx.jk.pojo.vo.req.page.DictItemPageReqVo;

public interface DictItemService extends IService<DictItem> {
  PageVo<DictItemVo> list(DictItemPageReqVo query);
}

