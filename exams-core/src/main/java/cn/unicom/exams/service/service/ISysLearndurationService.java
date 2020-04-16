package cn.unicom.exams.service.service;

import cn.unicom.exams.model.entity.SysLearnduration;
import cn.unicom.exams.model.vo.LearnedResourceEmpInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 王长何
 * @since 2020-03-20
 */
public interface ISysLearndurationService extends IService<SysLearnduration> {
    /**
     * 保存学习资料时长信息(接口)
     * @param empID
     * @param testID
     * @param resourceID
     * @param learnDuration
     * @throws Exception
     */
    public void saveLearnInfo(Long empID,Long testID,Long resourceID,Integer learnDuration) throws Exception;

    /**
     * 查询员工已学资源信息
     * @param page
     * @param limit
     * @param deptID
     * @return
     * @throws Exception
     */
    public IPage<LearnedResourceEmpInfo> getLearnedResEmpByPage(int page, int limit, Long deptID) throws Exception;

    /**
     * 查询7天员工学习次数
     * @return
     * @throws Exception
     */
    public List<Integer> getSenvenLearnResourceCount() throws Exception;

}
