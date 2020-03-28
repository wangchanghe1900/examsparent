package cn.unicom;

import cn.unicom.exams.service.utils.QuestionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
}
