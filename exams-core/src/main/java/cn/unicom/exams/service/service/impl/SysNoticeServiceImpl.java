package cn.unicom.exams.service.service.impl;

import cn.unicom.exams.model.entity.SysNotice;
import cn.unicom.exams.model.vo.NoticeVo;
import cn.unicom.exams.model.vo.UserInfo;
import cn.unicom.exams.service.mapper.SysNoticeMapper;
import cn.unicom.exams.service.service.ISysNoticeService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.security.auth.Subject;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 王长何
 * @since 2020-03-19
 */
@Service
public class SysNoticeServiceImpl extends ServiceImpl<SysNoticeMapper, SysNotice> implements ISysNoticeService {
    @Resource
     private SysNoticeMapper sysNoticeMapper;

    @Override
    public IPage<SysNotice> getSysNoticeInfoByCondition(int page, int limit, NoticeVo noticeVo) throws Exception {
        Page<SysNotice> iPage=new Page<>(page,limit);
        QueryWrapper<SysNotice> queryWrapper=new QueryWrapper<>();
        if(noticeVo != null){
            queryWrapper.likeRight(StringUtils.isNotEmpty(noticeVo.getTitle()),"title",noticeVo.getTitle());
        }
        queryWrapper.orderByDesc("createTime");
        return sysNoticeMapper.selectPage(iPage,queryWrapper);
    }
}
