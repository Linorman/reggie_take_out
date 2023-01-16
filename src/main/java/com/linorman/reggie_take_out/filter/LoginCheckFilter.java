package com.linorman.reggie_take_out.filter;

import com.alibaba.fastjson.JSON;
import com.linorman.reggie_take_out.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 登录检查过滤器
 * @author linorman
 * @since 2020-04-10
 */
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
@Slf4j
@Component
public class LoginCheckFilter implements Filter {

    // 路径匹配器
    private final PathMatcher pathMatcher = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        /*
         * 1.获取session
         */
        String uri = req.getRequestURI();
        log.info("拦截到请求：" + uri);
        // 定义url白名单
        String[] urlNotCheck = {"/employee/login", "/employee/logout", "/backend/**", "/front/**"};

        /*
          2.判断是否在白名单
         */
        if (isUrlNotCheck(uri, urlNotCheck)) {
            log.info("{}放行", uri);
            chain.doFilter(request, response);
        } else {
            HttpSession session = req.getSession();
            if (session.getAttribute("employee") != null) {
                log.info("用户已登录，{}放行", uri);
                chain.doFilter(request, response);
//            } else {
//                resp.sendRedirect("/login");
//            }
            } else {
                log.info("用户未登录，{}拦截", uri);
                resp.getWriter().write(JSON.toJSONString(R.error("NOLOGIN")));
            }
        }
    }

    public boolean isUrlNotCheck(String uri, String[] urlNotCheck) {
        for (String url : urlNotCheck) {
            if (pathMatcher.match(url, uri)) {
                return true;
            }
        }
        return false;
    }
}
