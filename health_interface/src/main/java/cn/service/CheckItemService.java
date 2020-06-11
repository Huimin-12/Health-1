package cn.service;


import cn.domain.CheckItem;
import cn.entity.PageResult;
import cn.entity.QueryPageBean;

import java.util.List;

/**
 * 检查项服务接口
 */
public interface CheckItemService {

    void add(CheckItem checkItem);

    PageResult pageQuer(QueryPageBean queryPageBean);

    void deleteOne(Integer id);

    CheckItem findById(int id);

    void handleEdit(CheckItem checkItem);

    List<CheckItem> findAll();
}
