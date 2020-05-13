package cn.unicom;

import cn.unicom.exams.service.utils.QuestionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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

    @Test
    public void encryptTest(){
        //String key= "\\x31\\x32\\x33\\x34\\x35\\x36\\x37\\x38\\x39\\x30\\x61\\x62\\x63\\x64\\x65\\x66";
/*        String key="1234567890abcdef";
        //String content="{\"userName\":\"111\",\"password\":\"222\",\"validcode\":\"\"}";
        //String encrypt = EncryptUtils.aesEncrypt(content, key, true, "");
        String encryCode="6cb5ea9bab83b6762659a126b8101423a0ed5f2c54b4ecaae2adfe1f3a0400653c4dd0c07eb9ce04d4ce799fb8f5757b794582eba8f7f5f28691b0e418c4a430";

        String decrypt = EncryptUtils.aesDecrypt(encryCode, key, false, key);
        System.out.println(decrypt);*/
    }



}
