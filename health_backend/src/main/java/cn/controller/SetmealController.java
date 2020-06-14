package cn.controller;

import cn.constant.MessageConstant;
import cn.constant.RedisConstant;
import cn.domain.Setmeal;
import cn.entity.PageResult;
import cn.entity.QueryPageBean;
import cn.entity.Result;
import cn.service.SetmealService;
import cn.utils.QiniuUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Reference
    private SetmealService setmealService;
    //注入redis 连接池
    @Autowired
    private JedisPool jedisPool;
    //上传图片
    @RequestMapping("/upload")
    public Result upload(@RequestParam(value = "imgFile") MultipartFile imgFile){
        //获取原始文件名
        String originalFilename = imgFile.getOriginalFilename();
        //对原始文件名进行从后以.切割
        int lastIndexOf = originalFilename.lastIndexOf(".");
        //获取文件名后缀
        String suffix = originalFilename.substring(lastIndexOf - 1);//.jsp
        //使用UUID随机产生文件名称，防止同名文件覆盖
        String fileName = UUID.randomUUID().toString()+suffix;
        try {
            //往七牛云上面存储图片
            QiniuUtils.upload2Qiniu(imgFile.getBytes(),fileName);
            //使用redis记录存储的名称
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES,fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }
        return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS,fileName);
    }

    //添加套餐
    @RequestMapping("/add")
    public Result add(@RequestBody Setmeal setmeal , Integer[] checkgroupIds){
        try {
            setmealService.add(setmeal,checkgroupIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ADD_SETMEAL_FAIL);
        }
        return new Result(true,MessageConstant.ADD_SETMEAL_SUCCESS);
    }
    //分页查询
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
       PageResult pageResult = setmealService.findPage(queryPageBean);
       return pageResult;
    }
}
