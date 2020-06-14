package cn.service;

import cn.domain.Setmeal;
import cn.entity.PageResult;
import cn.entity.QueryPageBean;

public interface SetmealService {
    //新增套餐
    void add(Setmeal setmeal, Integer[] checkgroupIds);
    //分页查询
    PageResult findPage(QueryPageBean queryPageBean);
}
