package cn.service.impl;

import cn.dao.CheckItemMapper;
import cn.domain.CheckItem;
import cn.entity.PageResult;
import cn.entity.QueryPageBean;
import cn.service.CheckItemService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 实现health_interface 当中创建出来的接口，并且实现接口当中的方法
 */
@Service(interfaceClass = CheckItemService.class)
@Transactional
public class CheckItemServiceImpl implements CheckItemService {
    //注入dao
    @Autowired
    private CheckItemMapper checkItemMapper;
    public void add(CheckItem checkItem) {
        //调用dao接口,
        checkItemMapper.add(checkItem);
    }

    @Override
    public PageResult pageQuer(QueryPageBean queryPageBean) {
        //分页代码，使用分页插件进行查询
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        //根据条件查询
        Page<CheckItem> page=checkItemMapper.selectByCondition(queryPageBean.getQueryString());
        return new PageResult(page.getTotal(),page.getResult());

    }
    //根据id删除数据
    @Override
    public void deleteOne(Integer id) {
        //查询要删除的检查项是否被检查组关联，被关联是不允许被删除的
       long count = checkItemMapper.findCountByCheckItemId(id);
        if (count >0){
            //当前检查项被引用，不能删除
            throw new RuntimeException("当前检查项被引用，不能删除");
        }
        //调用接口当中的删除方法
        checkItemMapper.deleteOne(id);
    }
    //根据id进行查询
    @Override
    public CheckItem findById(int id) {
       CheckItem checkItem= checkItemMapper.findById(id);
        return checkItem ;
    }
    //修改数据
    @Override
    public void handleEdit(CheckItem checkItem) {
        checkItemMapper.handleEdit(checkItem);
    }
    //查询所有数据
    @Override
    public List<CheckItem> findAll() {

            List<CheckItem> checkItemList=checkItemMapper.findAll();
        return checkItemList;
    }

}
