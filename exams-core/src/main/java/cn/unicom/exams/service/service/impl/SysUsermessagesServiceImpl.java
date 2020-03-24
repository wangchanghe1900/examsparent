package cn.unicom.exams.service.service.impl;

import cn.unicom.exams.model.entity.SysUsermessages;
import cn.unicom.exams.model.vo.UserMessageInfo;
import cn.unicom.exams.model.vo.UserMessageVo;
import cn.unicom.exams.service.mapper.SysUsermessagesMapper;
import cn.unicom.exams.service.service.ISysUsermessagesService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 王长何
 * @since 2020-03-20
 */
@Service
public class SysUsermessagesServiceImpl extends ServiceImpl<SysUsermessagesMapper, SysUsermessages> implements ISysUsermessagesService {
    @Resource
    private SysUsermessagesMapper usermessagesMapper;
    @Override
    public IPage<UserMessageInfo> getUserMessageInfoByConditon(int page, int limit, UserMessageVo userMessageVo) throws Exception {
        Page<UserMessageVo> iPage=new Page<>(page,limit);
        QueryWrapper<UserMessageVo> qw=new QueryWrapper<>();
        if(userMessageVo!=null){
            if("admin".equals(userMessageVo.getUserName())){
                qw.eq(StringUtils.isNotEmpty(userMessageVo.getIsRead()),"isRead",userMessageVo.getIsRead())
                .likeRight(StringUtils.isNotEmpty(userMessageVo.getTitle()),"n.title",userMessageVo.getTitle());
            }else{
                qw.eq(StringUtils.isNotEmpty(userMessageVo.getUserCode()),"receviceUserCode",userMessageVo.getUserCode())
                        .eq(StringUtils.isNotEmpty(userMessageVo.getIsRead()),"isRead",userMessageVo.getIsRead())
                        .likeRight(StringUtils.isNotEmpty(userMessageVo.getTitle()),"n.title",userMessageVo.getTitle());
            }
        }
        qw.orderByDesc("m.createDate");
        return usermessagesMapper.getUserMessageInfoByPage(iPage,qw);
    }

    @Override
    public Integer getUserMessageCount(String userName) throws Exception {
        QueryWrapper<UserMessageVo> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("u.username",userName)
                .eq("m.isRead","否");
        return usermessagesMapper.getUserMessageCount(queryWrapper);
    }


}
