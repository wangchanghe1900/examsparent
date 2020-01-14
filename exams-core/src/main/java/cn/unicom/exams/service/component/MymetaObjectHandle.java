package cn.unicom.exams.service.component;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author 王长何
 * @create 2020-01-12 15:00
 */
@Component
public class MymetaObjectHandle implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        boolean b = metaObject.hasSetter("createtime");
        if(b){
            Object createtime = getFieldValByName("createtime", metaObject);
            if(createtime == null){
                setInsertFieldValByName("createtime", LocalDateTime.now(),metaObject);
            }
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        boolean b = metaObject.hasSetter("lastlogintime");
/*        if(b){
            Object lastlogintime = getFieldValByName("lastlogintime", metaObject);
            if(lastlogintime ==null){
                setUpdateFieldValByName("lastlogintime", LocalDateTime.now(),metaObject);
            }
        }*/
    }
}
