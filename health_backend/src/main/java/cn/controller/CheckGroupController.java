package cn.controller;

import cn.constant.MessageConstant;
import cn.domain.CheckGroup;
import cn.entity.PageResult;
import cn.entity.QueryPageBean;
import cn.entity.Result;
import cn.service.CheckGroupService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/checkGroup")
public class CheckGroupController {
    //注入service
    @Reference
    private CheckGroupService checkGroupService;
    //添加检查组
    @RequestMapping("/add")
    public Result add(@RequestBody CheckGroup checkGroup, Integer[] checkitemIds){

        try {
            checkGroupService.add(checkGroup,checkitemIds);
            return new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, MessageConstant.ADD_CHECKGROUP_FAIL);
        }
    }
    //分页查询检查组
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
           PageResult pageResult= checkGroupService.findPage(queryPageBean);
           return pageResult;
    }
    //根据id查询数据
    @RequestMapping("/findById/{id}")
    public Result findById(@PathVariable(value = "id") int id){

        try {
            CheckGroup checkGroup = checkGroupService.findById(id);
            return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkGroup);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_CHECKGROUP_FAIL);
        }

    }
}
