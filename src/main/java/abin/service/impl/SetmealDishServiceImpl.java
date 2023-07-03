package abin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import abin.entity.SetmealDish;
import abin.mapper.SetmealDishMapper;
import abin.service.SetmealDishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SetmealDishServiceImpl extends ServiceImpl<SetmealDishMapper,SetmealDish> implements SetmealDishService {
}
