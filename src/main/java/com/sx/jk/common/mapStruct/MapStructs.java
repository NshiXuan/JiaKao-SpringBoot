package com.sx.jk.common.mapStruct;

import com.sx.jk.pojo.po.*;
import com.sx.jk.pojo.vo.LoginVo;
import com.sx.jk.pojo.vo.list.*;
import com.sx.jk.pojo.vo.req.save.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {
        // 使用什么转换器
        MapStructFormatter.class
})
public interface MapStructs {
  MapStructs INSTANCE = Mappers.getMapper(MapStructs.class);

  /* Po -> Vo */
  DictItemVo po2vo(DictItem po);

  DictTypeVo po2vo(DictType po);

  ExamPlaceVo po2vo(ExamPlace po);

  PlateRegionVo po2vo(PlateRegion po);

  ExamPlaceCourseVo po2vo(ExamPlaceCourse po);

  // 说明哪个属性转成哪个属性 qualifiedBy表示使用有Date2Millis注解的方法转换
  @Mapping(source = "loginTime",
          target = "loginTime",
          qualifiedBy = MapStructFormatter.Date2Millis.class)
  SysUserVo po2vo(SysUser po);

  LoginVo po2loginVo(SysUser po);

  SysRoleVo po2vo(SysRole po);

  SysResourceVo po2vo(SysResource po);

  /* ReqVo -> Po */
  DictItem reqVo2po(DictItemReqVo reqVo);

  DictType reqVo2po(DictTypeReqVo reqVo);

  ExamPlace reqVo2po(ExamPlaceReqVo reqVo);

  PlateRegion reqVo2po(PlateRegionReqVo reqVo);

  ExamPlaceCourse reqVo2po(ExamPlaceCourseReqVo reqVo);

  SysUser reqVo2po(SysUserReqVo reqVo);

  SysRole reqVo2po(SysRoleReqVo reqVo);

  SysResource reqVo2po(SysResourceReqVo reqVo);
}
