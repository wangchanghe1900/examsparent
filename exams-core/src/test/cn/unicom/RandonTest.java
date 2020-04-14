package cn.unicom;

import cn.unicom.exams.model.entity.SysDept;
import cn.unicom.exams.model.vo.DeptInfo;
import cn.unicom.exams.service.mapper.SysDeptMapper;
import cn.unicom.exams.service.service.ISysDeptService;
import cn.unicom.exams.service.utils.QuestionUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author 王长何
 * @create 2020-03-25 12:48
 */
@RunWith(SpringRunner.class)
@SpringBootTest()
@SpringBootConfiguration
public class RandonTest {


    @Test
    public void test(){
        List<Long> idList=new ArrayList<>();
        for (int i = 0; i <100 ; i++) {
            idList.add(new Long(i));
        }
        try{
            Set<Long> idSet = QuestionUtils.randomQuestions(idList, 30);
            idSet.stream().forEach(System.out::println);
            System.out.println(idSet.size());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void scoreTest(){
        Set<Long> idsSet=new HashSet<>();
        Random random=new Random();
        random.nextInt(200);
        while(true){
            if(idsSet.size()==14) break;
            idsSet.add(Long.valueOf(random.nextInt(200)));
        }
        List<Long> ids=new ArrayList<>(idsSet);
        Map<Long, Integer> longIntegerMap = QuestionUtils.randomQuestionScore(ids, 14);
        Integer result=0;
        Set<Map.Entry<Long, Integer>> entries = longIntegerMap.entrySet();
        for(Map.Entry<Long, Integer> entry: entries){
            System.out.println(entry.getKey()+":"+entry.getValue());
        }
        int sum = entries.stream().map(e -> e.getValue()).reduce(0, Integer::sum).intValue();
        System.out.println("==========="+sum);
    }


}
