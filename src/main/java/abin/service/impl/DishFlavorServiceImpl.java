package abin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import abin.entity.DishFlavor;
import abin.mapper.DishFlavorMapper;
import abin.service.DishFlavorService;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper,DishFlavor> implements DishFlavorService {
}
