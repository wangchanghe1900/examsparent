package cn.unicom.exams.service.service.impl;

import cn.unicom.exams.model.entity.SysTestpaper;
import cn.unicom.exams.model.vo.TestPaperInfo;
import cn.unicom.exams.model.vo.TestPaperVo;
import cn.unicom.exams.service.mapper.SysTestpaperMapper;
import cn.unicom.exams.service.service.ISysTestpaperService;
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
 * @since 2020-03-02
 */
@Service
public class SysTestpaperServiceImpl extends ServiceImpl<SysTestpaperMapper, SysTestpaper> implements ISysTestpaperService {
    @Resource
    private SysTestpaperMapper testpaperMapper;
    @Override
    public IPage<TestPaperInfo> getTestPaperInfoByPage(int page, int limit, TestPaperVo testPaperVo) throws Exception{
        Page<TestPaperVo> iPage=new Page<>(page,limit);
        QueryWrapper<TestPaperVo> queryWrapper=new QueryWrapper<>();
        if(testPaperVo!=null){
            if("admin".equals(testPaperVo.getUserName())){
                queryWrapper.likeRight(StringUtils.isNotEmpty(testPaperVo.getTestName()),"testName",testPaperVo.getTestName());

            }else{
                queryWrapper.likeRight(StringUtils.isNotEmpty(testPaperVo.getTestName()),"testName",testPaperVo.getTestName())
                        .eq(testPaperVo.getDeptId()!=null,"a.dept_id",testPaperVo.getDeptId());
            }
            queryWrapper.orderByDesc("a.createTime").orderByDesc("a.id");
        }
        return testpaperMapper.getTestPaperInfoByPage(iPage,queryWrapper);
    }
}
