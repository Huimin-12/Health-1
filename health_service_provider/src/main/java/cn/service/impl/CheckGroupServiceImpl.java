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
import java.util.List;
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
        //调用抽取出来的方法进行添加数据
        this.checkGroupIdAndCheckitemId(checkGroupId,checkitemIds);
    }
    //分页查询+条件查询
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        //使用spring提供的分页插件进行分页查询
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        Page<CheckGroup> checkGroups=checkGroupMapper.selectByCondition(queryPageBean.getQueryString());
        return new PageResult(checkGroups.getTotal(),checkGroups.getResult());
    }
    //根据id查询检查组信息
    @Override
    public CheckGroup findById(int id) {
       CheckGroup checkGroup= checkGroupMapper.findById(id);
        return checkGroup;
    }
    //根据检查组id查询关联的检查项
    @Override
    public List<Integer> findCheckItemIdsByCheckGroupId(Integer id) {
        List<Integer> checkitemIds = checkGroupMapper.findCheckItemIdsByCheckGroupId(id);
        return checkitemIds;
    }
    //编辑检查组
    @Override
    public void edit(CheckGroup checkGroup, Integer[] checkitemIds) {
        //编辑更新检查组信息
        checkGroupMapper.edit(checkGroup);
        //删除t_checkgroup_checkitem多对多的关联关系
        Integer checkGroupId = checkGroup.getId();
        checkGroupMapper.deleteAssociation(checkGroupId);
        //重新添加t_checkgroup_checkitem多对多的关联关系
        this.checkGroupIdAndCheckitemId(checkGroupId,checkitemIds);
    }
    //封装一个方法
    public void checkGroupIdAndCheckitemId(Integer checkGroupId,Integer[] checkitemIds){
        //遍历数组
        for (Integer checkitemId : checkitemIds) {
            //使用map集合进行封装
            Map<String,Integer> map = new HashMap<String,Integer>();
            map.put("checkGroupId",checkGroupId);
            map.put("checkitemId",checkitemId);
            checkGroupMapper.set_checkGroupIdAndCheckitemId(map);
        }
    }
}
