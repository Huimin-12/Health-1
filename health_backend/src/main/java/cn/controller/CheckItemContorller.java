package cn.controller;

import cn.constant.MessageConstant;
import cn.domain.CheckItem;
import cn.entity.PageResult;
import cn.entity.QueryPageBean;
import cn.entity.Result;
import cn.service.CheckItemService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *
 */
@RestController
@RequestMapping("/checkitem")
public class CheckItemContorller {

    @Reference
   private CheckItemService checkItemService;
    //添加检查项
    @RequestMapping("/add")
    public Result add(@RequestBody CheckItem checkItem){
        //调用checkItemService中的方法，进行数据的添加操作
        try {
            checkItemService.add(checkItem);
        } catch (Exception e) {
            e.printStackTrace();
            //报错直接返回错误信息，新增检查项失败
            return new Result(false, MessageConstant.ADD_CHECKITEM_FAIL);
        }
        //代码执行到这里说明操作成功，新增检查项成功
        return new Result(true,MessageConstant.ADD_CHECKITEM_SUCCESS);
    }
    //分页查询
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult = checkItemService.pageQuer(queryPageBean);
        return pageResult;
    }
    //根据id删除数据
    @RequestMapping("/deleteOne/{id}")
    public Result deleteOne(@PathVariable(value = "id") Integer id){

        try {
            checkItemService.deleteOne(id);
        } catch (Exception e) {
            e.printStackTrace();
            //报错直接返回错误信息，删除检查项失败
            return new Result(false,MessageConstant.DELETE_CHECKITEM_FAIL);
        }
        //操作执行成功，删除检查项成功
        return new Result(true,MessageConstant.DELETE_CHECKITEM_SUCCESS);
    }
    //根据id查询数据，用于回显数据方法
    @RequestMapping("/findById/{id}")
    public Result findById(@PathVariable(value = "id") int id){
        try {
            //根据id查询出数据，封装到实体类当中
            CheckItem checkItem = checkItemService.findById(id);
            //查询检查项成功
            return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItem);
        } catch (Exception e) {
            e.printStackTrace();
            //查询检查项失败
            return new Result(false,MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }
    //保存修改后的数据
    @RequestMapping("/handleEdit")
    public Result handleEdit(@RequestBody CheckItem checkItem){

        try {
            checkItemService.handleEdit(checkItem);
        } catch (Exception e) {
            e.printStackTrace();
            //编辑检查项失败
            return new Result(false,MessageConstant.EDIT_CHECKITEM_FAIL);
        }
        //编辑检查项成功
        return new Result(true,MessageConstant.EDIT_CHECKITEM_SUCCESS);
    }
    //查询所有检查项
    @RequestMapping("/findAll")
    public Result findAll(){
        try {
            List<CheckItem> checkItemList = checkItemService.findAll();
            return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItemList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true,MessageConstant.ADD_CHECKITEM_FAIL);
        }
    }
}
