package cn.unicom.exams.service.service;

import cn.unicom.exams.model.entity.SysResourceinfo;
import cn.unicom.exams.model.vo.ResourceInfo;
import cn.unicom.exams.model.vo.ResourceVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 王长何
 * @since 2020-02-26
 */
public interface ISysResourceinfoService extends IService<SysResourceinfo> {
    /**
     * 根据条件提取资源信息并分页
     * @param page
     * @param limit
     * @param resourceVo
     * @return
     * @throws Exception
     */
    public IPage<ResourceInfo> getResourceInfoByPage(int page, int limit, ResourceVo resourceVo) throws Exception;


}
