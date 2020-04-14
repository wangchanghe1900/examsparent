package cn.unicom.exams.service.service;

import cn.unicom.exams.model.entity.SysDeptreturn;
import cn.unicom.exams.model.vo.DeptTestStatisInfo;
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
public interface ISysDeptreturnService extends IService<SysDeptreturn> {
    /**
     * 统计部门考试返回次数
     * @param page
     * @param limit
     * @param deptID
     * @return
     * @throws Exception
     */
    public IPage<DeptTestStatisInfo> getDeptTestStatisInfo(int page, int limit, Long deptID) throws Exception;

}
