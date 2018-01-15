package com.ruolan.springtest.interceptor;

import com.ruolan.springtest.entity.PersonInfo;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class LoginInterceptor extends HandlerInterceptorAdapter {


    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {

        System.out.println("LoginInterceptor    到这了");

        Object userObj = request.getSession().getAttribute("user");
        if (userObj != null) {
            PersonInfo user = (PersonInfo) userObj;
            if (user != null && user.getUserId() != null
                    && user.getUserId() > 1
                    && user.getEnableStatus() == 1
                    && user.getShopOwnerFlag() == 1)
                return true;
        }
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<script>");
        //在这个地方要去登录设置路径
//        out.println("window.open ('" + request.getContextPath()
//                + "/shop/ownerlogin','_self')");
        out.println("</script>");
        out.println("</html>");
        return false;
    }

}
