package com.person.service;

import com.person.pojo.ChangeStatusDTO;
import com.person.pojo.dto.AppInfoDTO;
import com.person.pojo.dto.RegisterAppDTO;
import com.person.pojo.dto.UnRegisterAppDTO;
import com.person.pojo.vo.AppVO;

import java.util.List;

/**
 * @Description 应用接口层
 * @Author Xutong Li
 * @Date 2021/3/8
 */
public interface IAppService {

    /**
     * @Description 应用注册
     * @Author Xutong Li
     * @Date 2021/3/8
     * @param registerAppDTO
     * @return void
     */
    void register(RegisterAppDTO registerAppDTO);

    /**
     * @Description 应用注销
     * @Author Xutong Li
     * @Date 2021/3/9
     * @param unRegisterAppDTO
     * @return void
     */
    void unRegister(UnRegisterAppDTO unRegisterAppDTO);

    /**
     * @Description 获取应用信息集合
     * @Author Xutong Li
     * @Date 2021/3/9
     * @param
     * @return java.util.List<com.person.pojo.vo.AppVO>
     */
    List<AppVO> getList();

    /**
     * @Description 更新启用状态
     * @Author Xutong Li
     * @Date 2021/5/1
     * @param changeStatusDTO
     * @return void
     */
    void updateEnabled(ChangeStatusDTO changeStatusDTO);

    /**
     * @Description 删除服务
     * @Author Xutong Li
     * @Date 2021/5/1
     * @param id
     * @return void
     */
    void delete(Integer id);

    /**
     * @Description 获取服务信息集合（通过服务名集合）
     * @Author Xutong Li
     * @Date 2021/5/1
     * @param appNameList
     * @return java.util.List<com.person.pojo.dto.AppInfoDTO>
     */
    List<AppInfoDTO> getAppInfos(List<String> appNameList);
}
