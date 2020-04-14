package cn.unicom.exams.model.vo;

import lombok.Data;

import java.util.List;

/**
 * @author 王长何
 * @create 2020-03-27 16:49
 */
@Data
public class UnLearnResource {

    private  Long empID;
    private  Long departmentID;
    private Integer showNum;
    private Integer pageNum;
    private Long totalNum;
    private List<Material> materialList;
}
