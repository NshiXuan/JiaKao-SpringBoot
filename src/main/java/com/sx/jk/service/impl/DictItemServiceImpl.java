package com.sx.jk.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sx.jk.common.enhance.MyPage;
import com.sx.jk.common.enhance.MpLambdaQueryWrapper;
import com.sx.jk.common.mapStruct.MapStructs;
import com.sx.jk.mapper.DictItemDao;
import com.sx.jk.pojo.po.DictItem;
import com.sx.jk.pojo.vo.PageVo;
import com.sx.jk.pojo.vo.list.DictItemVo;
import com.sx.jk.pojo.vo.req.page.DictItemPageReqVo;
import com.sx.jk.service.DictItemService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("dictItemService")
@Transactional
public class DictItemServiceImpl extends ServiceImpl<DictItemDao, DictItem> implements DictItemService {

  @Override
  @Transactional(readOnly = true)
  public PageVo<DictItemVo> list(DictItemPageReqVo query) {
    // 查询条件
    MpLambdaQueryWrapper<DictItem> wrapper = new MpLambdaQueryWrapper<>();
    wrapper.like(query.getKeyword(), DictItem::getName, DictItem::getValue);
    Integer typeId = query.getTypeId();
    if (typeId != null && typeId > 0) {
      wrapper.eq(DictItem::getTypeId, typeId);
    }

    // 排序
    wrapper.orderByDesc(DictItem::getId);

    // 查询
    return baseMapper.
            selectPage(new MyPage<>(query), wrapper).
            buildVo(MapStructs.INSTANCE::po2vo);
  }
}

