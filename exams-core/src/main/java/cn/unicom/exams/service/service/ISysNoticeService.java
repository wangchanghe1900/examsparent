package cn.unicom.exams.service.service;

import cn.unicom.exams.model.entity.SysNotice;
import cn.unicom.exams.model.vo.NoticeVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 王长何
 * @since 2020-03-19
 */
public interface ISysNoticeService extends IService<SysNotice> {
    /**
     * 根据条件查询公告信息
     * @param page
     * @param limit
     * @param noticeVo
     * @return
     */
    public IPage<SysNotice> getSysNoticeInfoByCondition(int page, int limit, NoticeVo noticeVo) throws Exception;

}
