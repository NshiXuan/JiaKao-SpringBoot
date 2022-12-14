package com.sx.jk.common.enhance;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sx.jk.common.util.Streams;
import com.sx.jk.pojo.vo.PageVo;
import com.sx.jk.pojo.vo.req.page.PageReqVo;

import java.util.List;
import java.util.function.Function;

public class MyPage<T> extends Page<T> {
  private final PageReqVo reqVo;

  public MyPage(PageReqVo reqVo) {
    super(reqVo.getPage(), reqVo.getSize());
    this.reqVo = reqVo;
  }

  private <N> PageVo<N> commonBuildVo(List<N> data) {
    reqVo.setPage(getCurrent());
    reqVo.setSize(getSize());

    PageVo<N> pageVo = new PageVo<>();
    pageVo.setCount(getTotal());
    pageVo.setPages(getPages());
    pageVo.setData(data);
    return pageVo;
  }

  public PageVo<T> buildVo() {
    return commonBuildVo(getRecords());
  }

  public <R> PageVo<R> buildVo(Function<T, R> function) {
    return commonBuildVo(Streams.map(getRecords(), function));
  }
}
