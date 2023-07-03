package abin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import abin.entity.OrderDetail;
import abin.mapper.OrderDetailMapper;
import abin.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {

}