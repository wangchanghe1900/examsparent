package cn.unicom.exams.service.mapper;

import cn.unicom.exams.model.entity.SysUsermessages;
import cn.unicom.exams.model.vo.UserMessageInfo;
import cn.unicom.exams.model.vo.UserMessageVo;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 王长何
 * @since 2020-03-20
 */
public interface SysUsermessagesMapper extends BaseMapper<SysUsermessages> {
    /**
     * 根据条件查询系统信息
     * @param page
     * @param wrapper
     * @return
     * @throws Exception
     */
    public IPage<UserMessageInfo> getUserMessageInfoByPage(Page<UserMessageVo> page, @Param(Constants.WRAPPER) Wrapper<UserMessageVo> wrapper) throws Exception;

    /**
     * 根据条件查询系统用户未读信息
     * @param wrapper
     * @return
     * @throws Exception
     */
    public Integer getUserMessageCount(@Param(Constants.WRAPPER) Wrapper<UserMessageVo> wrapper)throws Exception;

}
