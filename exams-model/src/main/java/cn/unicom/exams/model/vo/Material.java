package cn.unicom.exams.model.vo;

import lombok.Data;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author 王长何
 * @create 2020-03-27 16:53
 */
@Data
public class Material {
    private Long materialID;
    private Integer materialType;
    private String materialName;
    private String materialURL;
    private Long examID;
    private String examName;
    private Integer examNum;
    private String materialImg;
    private LocalDateTime publishDate;
    private LocalDate startDate;
    private LocalDate finishDate;
}
