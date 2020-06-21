package cn.service.impl;

import cn.constant.RedisConstant;
import cn.dao.SetmealMapper;
import cn.domain.Setmeal;
import cn.entity.PageResult;
import cn.entity.QueryPageBean;
import cn.service.SetmealService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private JedisPool jedisPool;
    @Override
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        //调用表单添加方法，对数据进行存储
        setmealMapper.add(setmeal);
        //获取提交表单当中img图片的名称
        String fileName = setmeal.getImg();
        //将图片名称，存储到redis当中
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,fileName);
        Integer setmealId = setmeal.getId();
        //调用setSetmealAndCheckGroup方法
        this.setSetmealAndCheckGroup(setmealId,checkgroupIds);
    }
    //分页查询
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        //使用分页插件进行分页
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        //调用条件查询方法
        Page<Setmeal> setmealPage = setmealMapper.selectByCondition(queryPageBean.getQueryString());
        return new PageResult(setmealPage.getTotal(),setmealPage.getResult());
    }
    //查询所有套餐的数据
    @Override
    public List<Setmeal> findAll() {
        return setmealMapper.findAll();
    }
    //根据id进行套餐数据的查询
    @Override
    public Setmeal findById(int id) {
        Setmeal setmeal = setmealMapper.findById(id);
        return setmeal;
    }
    //查询用户套餐占比数，返回页面进行饼形图展示
    @Override
    public List<Map<String, Object>> findSetmealCount() {
        return setmealMapper.findSetmealCount();

    }

    //定义一个方法，用来遍历存储表与表之间的关系
    public void setSetmealAndCheckGroup(Integer setmealId,Integer[] checkgroupIds){
        //遍历数组
        for (Integer checkgroupId : checkgroupIds) {
            //创建一个map集合，用来存储相对应的键值对
            Map<String,Integer> map = new HashMap<String,Integer>();
            map.put("setmealId",setmealId);
            map.put("checkgroupId",checkgroupId);
            setmealMapper.setSetmealAndCheckGroup(map);
        }
    }
}
