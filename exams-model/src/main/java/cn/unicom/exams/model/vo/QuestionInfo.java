package cn.unicom.exams.model.vo;

import cn.unicom.exams.model.entity.SysOptions;
import cn.unicom.exams.model.entity.SysQuestions;
import cn.unicom.exams.model.entity.SysResourceinfo;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.List;

/**
 * @author 王长何
 * @create 2020-03-11 15:35
 */
@Data
public class QuestionInfo extends SysQuestions {
    /**
     * 考题所属资源
     */
    private SysResourceinfo resourceinfo;

    /**
     * 考题选项
     */
    private List<SysOptions> optionsList;
    /**
     * 编辑按钮权限
     */
    @TableField(exist = false)
    private Boolean isEdit;
    /**
     * 删除按钮权限
     */
    @TableField(exist = false)
    private Boolean isDel;


    /**
     * 查看详情按钮权限
     */
    private Boolean isDetail;
}
