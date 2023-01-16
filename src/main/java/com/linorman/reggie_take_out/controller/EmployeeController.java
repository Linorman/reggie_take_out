package com.linorman.reggie_take_out.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linorman.reggie_take_out.Service.EmployeeService;
import com.linorman.reggie_take_out.common.R;
import com.linorman.reggie_take_out.entity.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登陆
     * @param request
     * @param employee
     * @return
     */
    @RequestMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        /*
          1.密码加密
         */
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        /*
          2.根据用户名和密码查询员工
         */
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee res = employeeService.getOne(queryWrapper);

        /*
          3.判断员工是否存在
         */
        if (res == null) {
            return R.error("用户名不存在");
        }

        /*
          4.判断密码是否正确
         */
        if (!res.getPassword().equals(password)) {
            return R.error("密码错误");
        }

        /*
          5.查看员工是否被禁用
         */
        if (res.getStatus() == 0) {
            return R.error("该用户已被禁用");
        }

        /*
          6.登陆成功，将员工信息存入session，返回员工信息
         */
        request.getSession().setAttribute("employee", res.getId());
        log.info("Employee login: {}", employee);
        return R.success(res);

    }

    /**
     * 员工登出
     * @param request
     * @return
     */
    @RequestMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        /*
          1.清除session
         */
        request.getSession().removeAttribute("employee");
        /*
          2.返回成功信息
         */
        return R.success("登出成功");
    }
}
