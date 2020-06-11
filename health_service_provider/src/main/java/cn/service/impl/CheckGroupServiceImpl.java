package cn.service.impl;

import cn.dao.CheckGroupMapper;
import cn.domain.CheckGroup;
import cn.entity.PageResult;
import cn.entity.QueryPageBean;
import cn.service.CheckGroupService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * 检查组服务
 */
@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {
    @Autowired
    private CheckGroupMapper checkGroupMapper;
    //添加检查组
    @Override
    public void add(CheckGroup checkGroup, Integer[] checkitemIds) {
        //把checkGroup存入表当中
        checkGroupMapper.add(checkGroup);
        //可以通过checkGroup获取到id
        Integer checkGroupId = checkGroup.getId();
        //遍历数组
        for (Integer checkitemId : checkitemIds) {
            //使用map集合进行封装
            Map<String,Integer> map = new HashMap<String,Integer>();
            map.put("checkGroupId",checkGroupId);
            map.put("checkitemId",checkitemId);
            checkGroupMapper.set_checkGroupIdAndCheckitemId(map);
        }
    }
    //分页查询+条件查询
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        //使用spring提供的分页插件进行分页查询
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        Page<CheckGroup> checkGroups=checkGroupMapper.selectByCondition(queryPageBean.getQueryString());
        return new PageResult(checkGroups.getTotal(),checkGroups.getResult());
    }

    @Override
    public CheckGroup findById(int id) {
       CheckGroup checkGroup= checkGroupMapper.findById(id);
        return checkGroup;
    }

}
