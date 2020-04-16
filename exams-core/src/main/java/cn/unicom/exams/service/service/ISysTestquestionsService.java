package cn.unicom.exams.service.service;

import cn.unicom.exams.model.entity.SysTestquestions;
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
public interface ISysTestquestionsService extends IService<SysTestquestions> {
    /**
     * 查询7天内员工答题数量
     * @return
     * @throws Exception
     */
    public List<Integer> getSevenAnswerCount() throws Exception;



}
