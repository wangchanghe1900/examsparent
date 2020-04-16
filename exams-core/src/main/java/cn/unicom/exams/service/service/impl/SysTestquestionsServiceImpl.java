package cn.unicom.exams.service.service.impl;

import cn.unicom.exams.model.entity.SysTestquestions;
import cn.unicom.exams.service.mapper.SysTestquestionsMapper;
import cn.unicom.exams.service.service.ISysTestquestionsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 王长何
 * @since 2020-03-20
 */
@Service
public class SysTestquestionsServiceImpl extends ServiceImpl<SysTestquestionsMapper, SysTestquestions> implements ISysTestquestionsService {
    @Resource
    private SysTestquestionsMapper testquestionsMapper;
    @Override
    public List<Integer> getSevenAnswerCount() throws Exception {
        return testquestionsMapper.getSevenAnswerCount();
    }
}
