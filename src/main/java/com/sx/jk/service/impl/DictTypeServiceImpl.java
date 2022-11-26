package com.sx.jk.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sx.jk.common.enhance.MyPage;
import com.sx.jk.common.enhance.MpLambdaQueryWrapper;
import com.sx.jk.common.mapStruct.MapStructs;
import com.sx.jk.mapper.DictTypeDao;
import com.sx.jk.pojo.po.DictType;
import com.sx.jk.pojo.vo.PageVo;
import com.sx.jk.pojo.vo.list.DictTypeVo;
import com.sx.jk.pojo.vo.req.page.DictTypePageReqVo;
import com.sx.jk.service.DictTypeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service("dictTypeService")
@Transactional
public class DictTypeServiceImpl extends ServiceImpl<DictTypeDao, DictType> implements DictTypeService {

  @Override
  @Transactional(readOnly = true)
  public PageVo<DictTypeVo> list(DictTypePageReqVo query) {
    MpLambdaQueryWrapper<DictType> wrapper = new MpLambdaQueryWrapper<>();
    wrapper.like(query.getKeyword(), DictType::getName, DictType::getValue, DictType::getIntro);
    // 按照id降序
    wrapper.orderByDesc(DictType::getId);
    // 分页查询 selectPage第一个参数传过去什么类型就返回什么类型
    return baseMapper.
            selectPage(new MyPage<>(query), wrapper).
            buildVo(MapStructs.INSTANCE::po2vo);
  }
}

