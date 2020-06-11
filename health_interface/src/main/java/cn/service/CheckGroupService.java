package cn.service;

import cn.domain.CheckGroup;
import cn.entity.PageResult;
import cn.entity.QueryPageBean;

public interface CheckGroupService {
    //添加检查组
    void add(CheckGroup checkGroup, Integer[] checkitemIds);
    //分页查询
    PageResult findPage(QueryPageBean queryPageBean);

    CheckGroup findById(int id);
}
