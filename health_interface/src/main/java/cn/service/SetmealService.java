package cn.service;

import cn.domain.Setmeal;
import cn.entity.PageResult;
import cn.entity.QueryPageBean;

import java.util.List;
import java.util.Map;

public interface SetmealService {
    //新增套餐
    void add(Setmeal setmeal, Integer[] checkgroupIds);
    //分页查询
    PageResult findPage(QueryPageBean queryPageBean);
    //查询所有
    List<Setmeal> findAll();
    //根据id进行套餐数据的查询
    Setmeal findById(int id);

    List<Map<String, Object>> findSetmealCount();
}
