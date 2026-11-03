package com.hotel.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        if (uri.startsWith("/login") || uri.startsWith("/css/") || uri.startsWith("/js/") || uri.startsWith("/images/") || uri.startsWith("/fonts/") || uri.startsWith("/lib/")) {
            return true;
        }
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            if (isAjax(request)) {
                response.setContentType("application/json;charset=UTF-8");
                response.setStatus(401);
                response.getWriter().write("{\"code\":401,\"msg\":\"未登录\"}");
            } else {
                response.sendRedirect("/login");
            }
            return false;
        }
        return true;
    }

    private boolean isAjax(HttpServletRequest request) {
        String requestedWith = request.getHeader("X-Requested-With");
        return "XMLHttpRequest".equals(requestedWith);
    }
}
