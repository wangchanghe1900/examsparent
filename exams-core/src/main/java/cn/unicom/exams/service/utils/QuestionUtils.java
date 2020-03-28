package cn.unicom.exams.service.utils;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * @author 王长何
 * @create 2020-03-25 12:21
 */
@Component
public class QuestionUtils {
    public static Set<Long> randomQuestions(List<Long> idsList,Integer getCount) throws Exception{
        Set<Long> questiosSet=new HashSet<>();
        Random random=new Random();
        while(true){
            int id = random.nextInt(idsList.size());
            questiosSet.add(idsList.get(id));
            if(questiosSet.size()==getCount){
                break;
            }
        }
        return questiosSet;
    }
}
