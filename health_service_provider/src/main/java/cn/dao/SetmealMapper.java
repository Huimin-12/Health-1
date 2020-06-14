package cn.dao;

import cn.domain.Setmeal;
import com.github.pagehelper.Page;

import java.util.Map;

public interface SetmealMapper {

    void add(Setmeal setmeal);

    void setSetmealAndCheckGroup(Map<String, Integer> map);

    Page<Setmeal> selectByCondition(String queryString);
}
