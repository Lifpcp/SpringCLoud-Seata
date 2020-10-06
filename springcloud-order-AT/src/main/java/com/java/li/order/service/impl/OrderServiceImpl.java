package com.java.li.order.service.impl;

import com.java.li.common.model.Order;
import com.java.li.order.dao.OrderDao;
import com.java.li.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;

    @Override
    public void createOrder(Order order) {
        orderDao.save(order);
    }


}
