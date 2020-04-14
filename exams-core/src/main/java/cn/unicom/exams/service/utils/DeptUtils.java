package cn.unicom.exams.service.utils;

import cn.unicom.exams.model.entity.SysDept;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 王长何
 * @create 2020-04-13 12:12
 */
public class DeptUtils {
    public static List<Long> getDeptInfoByParentID(List<SysDept> deptList,Long parentID){
        List<Long> idList=new ArrayList<>();
        List<SysDept> childrenList = deptList.stream().filter(d -> d.getParentId().intValue() == parentID.intValue()).collect(Collectors.toList());
        if(childrenList.size()>0){
            for(SysDept dept : childrenList){
                idList.add(dept.getId());
                List<Long> childrenIdList = getDeptInfoByParentID(deptList, dept.getId());
                if(childrenIdList.size()>0){
                    idList.addAll(childrenIdList);
                }
            }

        }
        return idList;
    }

}
