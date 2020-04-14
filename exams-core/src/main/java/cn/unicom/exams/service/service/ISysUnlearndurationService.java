package cn.unicom.exams.service.service;

import cn.unicom.exams.model.entity.SysUnlearnduration;
import cn.unicom.exams.model.vo.UnlearnEmpInfo;
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
public interface ISysUnlearndurationService extends IService<SysUnlearnduration> {
    /**
     * 根据条件查询员工未学资源信息
     * @param page
     * @param limit
     * @param deptID
     * @return
     * @throws Exception
     */
    public IPage<UnlearnEmpInfo> getUnlearnResEmpInfoByPage(int page, int limit, Long deptID) throws Exception;

}
