package cn.unicom.exams.service.service;

import cn.unicom.exams.model.entity.SysRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 角色 服务类
 * </p>
 *
 * @author 王长河
 * @since 2019-12-31
 */
public interface ISysRoleService extends IService<SysRole> {
    /**
     * 根据用户ID查询角色
     * @param uId
     * @return
     * @throws Exception
     */
    public List<SysRole> getRoleInfoByUId(Long uId) throws Exception;

    /**
     * 根据id删除角色信息
     * @param id
     * @return
     * @throws Exception
     */
    public Boolean deleteRoleById(Long id) throws Exception;

}
