package com.person.controller;

import com.person.pojo.ChangeStatusDTO;
import com.person.pojo.dto.RegisterAppDTO;
import com.person.pojo.dto.UnRegisterAppDTO;
import com.person.pojo.vo.AppVO;
import com.person.pojo.vo.Result;
import com.person.service.IAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description 应用Controller层
 * @Author Xutong Li
 * @Date 2021/3/8
 */
@Controller
@RequestMapping("/app")
public class AppController {

    @Autowired
    private IAppService appService;

    /**
     * @Description 应用注册
     * @Author Xutong Li
     * @Date 2021/3/8
     * @param registerAppDTO       
     * @return void
     */
    @ResponseBody
    @PostMapping("/register")
    public void register(@RequestBody @Validated RegisterAppDTO registerAppDTO){
        appService.register(registerAppDTO);
    }

    /**
     * @Description 应用注销
     * @Author Xutong Li
     * @Date 2021/3/9
     * @param unRegisterAppDTO
     * @return void
     */
    @ResponseBody
    @PostMapping("/unregister")
    public void unRegister(@RequestBody UnRegisterAppDTO unRegisterAppDTO){
        appService.unRegister(unRegisterAppDTO);
    }

    /**
     * @Description 查询应用集合
     * @Author Xutong Li
     * @Date 2021/3/9
     * @param model       
     * @return java.lang.String
     */
    @GetMapping("/list")
    public String list(ModelMap model){
        List<AppVO> appVOList = appService.getList();
        model.put("appVOList", appVOList);
        return "appList";
    }

    /**
     * @Description 更新启用状态
     * @Author Xutong Li
     * @Date 2021/5/1
     * @param
     * @return com.person.pojo.vo.Result
     */
    @ResponseBody
    @PutMapping("/status")
    public Result updateEnabled(@RequestBody ChangeStatusDTO changeStatusDTO){
        appService.updateEnabled(changeStatusDTO);
        return Result.success();
    }

    /**
     * @Description 删除服务
     * @Author Xutong Li
     * @Date 2021/5/1
     * @param id
     * @return com.person.pojo.vo.Result
     */
    @ResponseBody
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable("id") Integer id){
        appService.delete(id);
        return Result.success();
    }
}
