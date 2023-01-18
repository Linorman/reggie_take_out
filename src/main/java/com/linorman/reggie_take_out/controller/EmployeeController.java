package com.linorman.reggie_take_out.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.linorman.reggie_take_out.Service.EmployeeService;
import com.linorman.reggie_take_out.common.R;
import com.linorman.reggie_take_out.entity.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登陆
     *
     * @param request
     * @param employee
     * @return R
     */
    @PostMapping("/login")
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
        request.getSession().setAttribute("employee", res.getName());
        log.info("Employee login: {}", employee);
        return R.success(res);

    }

    /**
     * 员工登出
     *
     * @param request
     * @return R
     */
    @PostMapping("/logout")
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

    /**
     * 添加员工
     *
     * @param request
     * @param employee
     * @return R
     */
    @PostMapping
    public R<String> add(HttpServletRequest request, @RequestBody Employee employee) {
        log.info("新增员工: {}", employee);
        /*
          1.设置默认密码并加密
         */
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        /*
          2.设置创建时间，更新时间，创建用户，更新用户
         */
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        String name = (String) request.getSession().getAttribute("employee");
        employee.setCreateUser(name);
        employee.setUpdateUser(name);

        /*
          3.添加员工
         */
        employeeService.save(employee);
        log.info("Employee add: {}", employee);
        return R.success("添加成功");
    }

    /**
     * 删除员工
     *
     * @param page
     * @param pageSize
     * @param name
     * @return R
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        log.info("page = {}, pageSize = {}, name = {}", page, pageSize, name);

        // 1.创建分页对象
        Page<Employee> pageObj = new Page<>(page, pageSize);

        // 2.创建查询条件
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        if (name != null && !name.equals("")) {
            queryWrapper.like(Employee::getName, name);
        }

        // 3.排序条件
        queryWrapper.orderByDesc(Employee::getUpdateTime);

        // 4.查询
        employeeService.page(pageObj, queryWrapper);

        // 5.返回
        return R.success(pageObj);
    }

    /**
     * 修改员工
     * @param employee
     * @param request
     * @return R
     */
    @PutMapping
    public R<String> update(HttpServletRequest request, @RequestBody Employee employee) {
        log.info("修改员工: {}", employee.toString());
        /*
          1.设置更新时间，更新用户
         */
        employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser((String) request.getSession().getAttribute("employee"));
        /*
          2.修改员工
         */
        employeeService.updateById(employee);
        log.info("Employee update: {}", employee);
        return R.success("修改成功");
    }

    /**
     * 根据id查询员工信息
     * @param id
     * @return R
     */
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){
        log.info("根据id查询员工信息...");
        Employee employee = employeeService.getById(id);
        if(employee != null){
            return R.success(employee);
        }
        return R.error("没有查询到对应员工信息");
    }
}
