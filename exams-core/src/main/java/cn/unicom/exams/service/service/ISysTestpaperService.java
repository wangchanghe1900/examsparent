package cn.unicom.exams.service.service;

import cn.unicom.exams.model.entity.SysTestpaper;
import cn.unicom.exams.model.vo.TestPaperInfo;
import cn.unicom.exams.model.vo.TestPaperVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 王长何
 * @since 2020-03-02
 */
public interface ISysTestpaperService extends IService<SysTestpaper> {
    /**
     * 根据条件查询试卷信息分页
     * @param page
     * @param limit
     * @param testPaperVo
     * @return
     */
    public IPage<TestPaperInfo> getTestPaperInfoByPage(int page, int limit, TestPaperVo testPaperVo) throws Exception;

    /**
     * 根据试卷ID发布考试试题
     * @param testId
     * @throws Exception
     */
    public void publishTest(Long testId,String status) throws Exception;

}
