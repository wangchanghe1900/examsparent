package cn.unicom.exams.service.service;

import cn.unicom.exams.model.entity.SysUsermessages;
import cn.unicom.exams.model.vo.UserMessageInfo;
import cn.unicom.exams.model.vo.UserMessageVo;
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
public interface ISysUsermessagesService extends IService<SysUsermessages> {
    /**
     * 根据条件查询用户信息
     * @param page
     * @param limit
     * @param userMessageVo
     * @return
     */
    public IPage<UserMessageInfo> getUserMessageInfoByConditon(int page, int limit, UserMessageVo userMessageVo) throws Exception;

    /**
     * 根据名称查询未读消息数据
     * @param userName
     * @return
     * @throws Exception
     */
    public Integer getUserMessageCount(String userName) throws Exception;

    /**
     * 更新信息状态
     * @param id
     * @param status
     * @throws Exception
     */
    public void updateMessage(Long id,String status) throws Exception;

}
