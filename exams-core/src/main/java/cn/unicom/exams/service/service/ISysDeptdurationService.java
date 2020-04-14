package cn.unicom.exams.service.service;

import cn.unicom.exams.model.entity.SysDeptduration;
import cn.unicom.exams.model.vo.DeptResourceStatisInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 王长何
 * @since 2020-03-20
 */
public interface ISysDeptdurationService extends IService<SysDeptduration> {
    /**
     * 统计部门学习资源情况
     * @param page
     * @param limit
     * @param deptID
     * @return
     * @throws Exception
     */
    public IPage<DeptResourceStatisInfo> getDeptResourceStatisInfo(int page,int limit,Long deptID) throws Exception;

}
