package cn.service;

import cn.domain.Order;
import cn.entity.Result;

import java.util.Map;

public interface OrderService {

    Result order(Map map) throws Exception;

    Map findById(int id) throws Exception;
}
