package cn.service;

import cn.domain.CheckGroup;
import cn.entity.PageResult;
import cn.entity.QueryPageBean;

import java.util.List;

public interface CheckGroupService {
    //添加检查组
    void add(CheckGroup checkGroup, Integer[] checkitemIds);
    //分页查询
    PageResult findPage(QueryPageBean queryPageBean);

    CheckGroup findById(int id);

    List<Integer> findCheckItemIdsByCheckGroupId(Integer id);
    //编辑检查组
    void edit(CheckGroup checkGroup, Integer[] checkitemIds);
    //查询所有检查组信息
    List<CheckGroup> findAll();
}
