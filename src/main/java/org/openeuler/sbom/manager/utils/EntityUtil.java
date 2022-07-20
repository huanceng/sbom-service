package org.openeuler.sbom.manager.utils;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EntityUtil {

    public static <T> List<T> castEntity(List<Map> list, Class<T> clazz) throws Exception {
        List<T> returnList = new ArrayList<T>();
        if (CollectionUtils.isEmpty(list)) {
            return returnList;
        }

        for (Map o : list) {
            T obj = (T) mapToObject(o, clazz);
            if (obj != null) {
                returnList.add(obj);
            }
        }
        return returnList;
    }

    public static <T> Page<T> castEntity(Page<Map> oldPage, Class<T> clazz) throws Exception {
        List<T> returnList = new ArrayList<T>();
        if (oldPage!=null&&oldPage.getSize()>0) {
            for (Map o : oldPage) {
                T obj = (T) mapToObject(o, clazz);
                if (obj != null) {
                    returnList.add(obj);
                }
            }
        }

        return new  PageImpl(returnList,oldPage.getPageable(),oldPage.getTotalElements());
    }

    public static <T> T mapToObject(Map<String, Object> map, Class<?> beanClass) throws Exception {
        if (map == null)
            return null;
        T obj = (T) beanClass.getDeclaredConstructor().newInstance();

        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            int mod = field.getModifiers();
            if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                continue;
            }

            field.setAccessible(true);
            field.set(obj, map.get(field.getName()));
        }
        return obj;
    }
}
