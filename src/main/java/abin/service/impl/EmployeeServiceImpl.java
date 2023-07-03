package abin.service.impl;

import abin.service.EmployeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import abin.entity.Employee;
import abin.mapper.EmployeeMapper;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper,Employee> implements EmployeeService {
}
