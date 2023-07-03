package abin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import abin.entity.ShoppingCart;
import abin.mapper.ShoppingCartMapper;
import abin.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {

}
