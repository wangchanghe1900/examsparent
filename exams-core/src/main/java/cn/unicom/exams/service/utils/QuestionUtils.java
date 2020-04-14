package cn.unicom.exams.service.utils;

import org.springframework.stereotype.Component;

import java.util.*;

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

    public static Map<Long,Integer> randomQuestionScore(List<Long> idsList,Integer testCount){
        Map<Long,Integer> scoreMap=new HashMap<>();
        //System.out.println("----------"+idsList.size());
        Integer everyScore=100 / testCount;
        //System.out.println("everyScore = " + everyScore);
        Integer model=100 % testCount;
        //System.out.println("model = " + model);
        if(model==0){
            for(Long id : idsList){
                scoreMap.put(id,everyScore);
            }
            return scoreMap;
        }
        Integer num= model / everyScore;
        if(num<1) {
            Integer random = new Random().nextInt(idsList.size());
            System.out.println("random = " + random);

            for (int i = 0; i < idsList.size(); i++) {
                if (i == random) {
                    //System.out.println("-----------" + idsList.get(i));
                    scoreMap.put(idsList.get(i), (everyScore + model));
                } else {
                    scoreMap.put(idsList.get(i), everyScore);
                }
            }
            return scoreMap;
        }else{
            Set<Integer> randomSet=new HashSet<>();
            while(true){
                if(randomSet.size()==num) break;
                randomSet.add(new Random().nextInt(idsList.size()));
            }
            int tmp=model % randomSet.size();
            int increment=model/randomSet.size();
            for (int i = 0; i < idsList.size(); i++) {
                int v=i;
                Optional<Integer> first = randomSet.stream().filter(x -> v == x).findFirst();
                if (first.isPresent()) {
                    scoreMap.put(idsList.get(i), (everyScore + increment));
                } else {
                    scoreMap.put(idsList.get(i), everyScore);
                }
            }
            Set<Map.Entry<Long, Integer>> entries = scoreMap.entrySet();
            for(Map.Entry<Long,Integer> entry :entries){
                entry.setValue(entry.getValue()+tmp);
                break;
            }
            return  scoreMap;
        }

    }
}
