package cn.dao;

import cn.domain.CheckGroup;
import com.github.pagehelper.Page;

import java.util.Map;

public interface CheckGroupMapper {
    //添加数据
    void add(CheckGroup checkGroup);
    //添加中间表t_checkgroup_checkitem数据
    void set_checkGroupIdAndCheckitemId(Map<String, Integer> map);
    //根据条件查询
    Page<CheckGroup> selectByCondition(String queryString);
    //根据id进行数据查询
    CheckGroup findById(int id);
}
