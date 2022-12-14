package com.sx.jk.common.foreign;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.jk.common.foreign.anno.ForeignCascade;
import com.sx.jk.common.foreign.anno.ForeignField;
import com.sx.jk.common.foreign.anno.ForeignField.ForeignFields;
import com.sx.jk.common.foreign.info.ForeignFieldInfo;
import com.sx.jk.common.foreign.info.ForeignTableInfo;
import com.sx.jk.common.util.Classes;
import com.sx.jk.common.util.JsonVos;
import com.sx.jk.common.util.Strings;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Aspect
@Component
public class ForeignAspect implements InitializingBean {
    private static final String FOREIGN_SCAN = "classpath*:com/sx/jk/pojo/po/**/*.class";
    @Autowired
    private ApplicationContext ctx;
    @Autowired
    private ResourceLoader resourceLoader;

    @Around("execution(* com.sx.jk.service..*.remove*(..))")
    public Object handleRemove(ProceedingJoinPoint point) throws Throwable {
        Object target = point.getTarget();
        if (!(target instanceof IService)) return point.proceed();

        // ??????Service??????
        Class<?> poCls = ((IService<?>) target).getEntityClass();

        // ??????
        ForeignTableInfo table = ForeignTableInfo.get(poCls);
        if (table == null) return point.proceed();

        // ??????
        ForeignFieldInfo mainField = table.getMainField();
        if (mainField == null) return point.proceed();

        // ??????????????????
        List<ForeignFieldInfo> subFields = mainField.getSubFields();
        if (CollectionUtils.isEmpty(subFields)) return point.proceed();

        // ???????????????
        Object arg = point.getArgs()[0];
        List<Object> ids;
        if (arg instanceof List) {
            ids = (List<Object>) arg;
        } else {
            ids = new ArrayList<>();
            ids.add(arg);
        }

        for (ForeignFieldInfo subField : subFields) {
            ForeignTableInfo subTable = subField.getTable();
            BaseMapper<Class<?>> mapper = getMapper(subTable.getCls());
            QueryWrapper<Class<?>> wrapper = new QueryWrapper<>();
            wrapper.in(subField.getColumn(), ids);
            if (mainField.getCascade() == ForeignCascade.DEFAULT) { // ??????
                Integer count = mapper.selectCount(wrapper);
                if (count == 0) continue;
                // JsonVos.raise(String.format("???%d??????%s????????????????????????????????????", count, subTable.getTable()));
                JsonVos.raise(String.format("???%d??????%s????????????????????????????????????", count, subTable.getTable()));
            } else { // ??????????????????
                mapper.delete(wrapper);
            }
        }
        return point.proceed();
    }

    @Around("execution(* com.sx.jk.service..*.save*(..)) || execution(* com.sx.jk.service..*.update*(..)) ")
    public Object handleSaveOrUpdate(ProceedingJoinPoint point) throws Throwable {
        Object target = point.getTarget();
        if (!(target instanceof IService)) return point.proceed();

        // ??????Service??????
        Class<?> poCls = ((IService<?>) target).getEntityClass();

        // ??????
        ForeignTableInfo table = ForeignTableInfo.get(poCls);
        if (table == null) return point.proceed();

        // ??????????????????
        Collection<ForeignFieldInfo> subFields = table.getSubFields().values();
        if (CollectionUtils.isEmpty(subFields)) return point.proceed();

        // ??????
        Object model = point.getArgs()[0];
        if (model.getClass() != poCls) {
            return point.proceed();
        }

        // ??????????????????
        for (ForeignFieldInfo subField : subFields) {
            List<ForeignFieldInfo> mainFields = subField.getMainFields();
            if (CollectionUtils.isEmpty(mainFields)) continue;
            // ?????????????????????1?????????????????????????????????????????????
            if (mainFields.size() > 1) continue;

            Object subValue = subField.getField().get(model);
            // ????????????????????????????????????????????????
            if (subValue == null) continue;

            // ?????????????????????
            ForeignFieldInfo mainField = mainFields.get(0);
            BaseMapper<Class<?>> mapper = getMapper(mainField.getTable().getCls());
            QueryWrapper<Class<?>> wrapper = new QueryWrapper<>();
            wrapper.eq(mainField.getColumn(), subValue);
            if (mapper.selectCount(wrapper) == 0) {
                // JsonVos.raise(String.format("%s=%s?????????", subField.getColumn(), subValue));
                JsonVos.raise(String.format("%s=%s?????????", subField.getColumn(), subValue));
            }
        }
        return point.proceed();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ResourcePatternResolver resolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
        Resource[] rs = resolver.getResources(FOREIGN_SCAN);
        if (rs.length == 0) {
            // JsonVos.raise("FOREIGN_SCAN???????????????????????????????????????");
            JsonVos.raise("FOREIGN_SCAN???????????????????????????????????????");
        }

        MetadataReaderFactory readerFactory = new CachingMetadataReaderFactory(resourceLoader);
        for (Resource r : rs) {
            parseCls(readerFactory.getMetadataReader(r).getClassMetadata().getClassName());
        }
    }

    private BaseMapper<Class<?>> getMapper(Class<?> poCls) {
        // return (BaseMapper<Class<?>>) ctx.getBean(Strings.firstLetterLowercase(poCls.getSimpleName()) + "Mapper");
        return (BaseMapper<Class<?>>) ctx.getBean(Strings.firstLetterLowercase(poCls.getSimpleName()) + "Dao");
    }

    private void parseCls(String clsName) throws Exception {
        Class<?> subCls = Class.forName(clsName);
        ForeignTableInfo subTable = ForeignTableInfo.get(subCls, true);
        Classes.enumerateFields(subCls, (subField, curCls) -> {
            ForeignField ff = subField.getAnnotation(ForeignField.class);
            parseForeignField(subTable, subField, ff);

            ForeignFields ffs = subField.getAnnotation(ForeignFields.class);
            if (ffs == null) return null;
            for (ForeignField subFf : ffs.value()) {
                parseForeignField(subTable, subField, subFf);
            }
            return null;
        });
    }

    private void parseForeignField(ForeignTableInfo subTable,
                                   Field subField,
                                   ForeignField ff) throws Exception {
        // ????????????ForeignField???????????????
        if (ff == null) return;
        // ????????????
        Class<?> mainCls = Classes.notObject(ff.mainTable(), ff.value());
        // ??????ForeignField???????????????????????????????????????mainCls???
        if (mainCls == null || mainCls.equals(Object.class)) return;

        // ????????????????????????
        Field mainField = Classes.getField(mainCls, ff.mainField());
        // ??????????????????????????????????????????
        if (mainField == null) return;

        // ??????????????????
        ForeignTableInfo mainTable = ForeignTableInfo.get(mainCls, true);
        ForeignFieldInfo subFieldInfo = subTable.getSubField(subField);
        ForeignFieldInfo mainFieldInfo = mainTable.getMainField(mainField);

        // ?????????????????????
        subFieldInfo.addMainField(mainFieldInfo);
        mainFieldInfo.addSubField(subFieldInfo);
    }
}
