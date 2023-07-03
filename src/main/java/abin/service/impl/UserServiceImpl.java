package abin.service.impl;

import abin.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import abin.entity.User;
import abin.mapper.UserMapper;
import abin.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService {
}
