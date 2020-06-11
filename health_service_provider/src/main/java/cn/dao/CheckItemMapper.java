package cn.dao;

import cn.domain.CheckItem;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 使用Mybaites,创建代理对象执行操作
 */
public interface CheckItemMapper {
   //添加
   //@Insert("insert into t_checkitem(code,name,sex,age,price,type,remark,attention) values(#{code},#{name},#{sex},#{age},#{price},#{type},#{remark},#{attention})")
    void add(CheckItem checkItem);
    //根据条件查询
    //@Select("select * from t_checkitem")
    Page<CheckItem> selectByCondition(String queryString);
    //根据id查询检查组与检查项之间的关联
    long findCountByCheckItemId(Integer id);
    //根据id进行删除
    //@Delete("delete from t_checkitem where id=#{id}")
    void deleteOne(int id);
    //根据id进行查询用户，用于回显表单数据
    //@Select("select * from t_checkitem where id=#{id}")
    CheckItem findById(int id);
    //根据id进行修改
    //@Update("update t_checkitem set code=#{code},name=#{name},sex=#{sex},age=#{age},price=#{price},type=#{type},remark=#{remark},attention=#{attention} where id=#{id}")
    void handleEdit(CheckItem checkItem);
    //查询所有数据
    List<CheckItem> findAll();
}
