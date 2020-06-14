package cn.jobs;

import cn.constant.RedisConstant;
import cn.entity.QueryPageBean;
import cn.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.Set;

public class ClearImgJob {
    //
    @Autowired
    private JedisPool jedisPool;

    public void clearImg(){
        //根据Redis中保存的两个set集合进行差值计算，获得垃圾图片名称集合
        Set<String> set = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        if (set!=null) {
            //遍历set获取到里面的差值图片名称
            for (String fileName : set) {
                //调用七牛云工具类进行图片的删除
                QiniuUtils.deleteFileFromQiniu(fileName);
                //然后还需要把redis当中的图面名称删除
                jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES,fileName);
                System.out.println("删除的图片名称有："+fileName);
            }
        }
    }
}
