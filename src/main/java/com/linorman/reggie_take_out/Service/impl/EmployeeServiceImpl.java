package com.linorman.reggie_take_out.Service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linorman.reggie_take_out.Service.EmployeeService;
import com.linorman.reggie_take_out.entity.Employee;
import com.linorman.reggie_take_out.mapper.EmployeeMapper;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

}
