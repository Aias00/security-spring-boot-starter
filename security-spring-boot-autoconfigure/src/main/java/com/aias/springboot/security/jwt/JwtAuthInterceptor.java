package com.aias.springboot.security.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;

/**
 * Created by cooper on 2018/1/17.
 */
@Component
public class JwtAuthInterceptor extends HandlerInterceptorAdapter {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    private String getParamsValue(String[] value) {
        if (value == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < value.length; i++) {
            sb.append(value[i]).append(",");
        }

        if (value.length == 1) {
            return sb.substring(1, sb.length() - 1);
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        String requestURL = request.getRequestURL().toString();

        Map<String, String[]> parameterMap = request.getParameterMap();
        StringBuilder sb = new StringBuilder("?");
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            String value = getParamsValue(entry.getValue());
            sb.append(entry.getKey()).append("=").append(value).append("&");
        }

        logger.info("Request URL: " + requestURL + sb);
        String headerKey = jwtTokenUtil.getHeaderKey();
        String authToken = request.getHeader(headerKey);
        if (authToken == null) {
            authToken = request.getParameter(headerKey);
            if (authToken == null) {
                authToken = (String) request.getSession()
                        .getAttribute(headerKey);
                if (authToken == null) {
                    Cookie[] cookies = request.getCookies();
                    if (cookies != null && cookies.length > 0) {
                        for (Cookie cookie : cookies) {
                            String name = cookie.getName();
                            if (!headerKey.equals(name)) {
                                continue;
                            }
                            authToken = cookie.getValue();
                        }
                    }
                }
            }
        }
        if (authToken != null) {
            String username = jwtTokenUtil.getUsernameFromToken(authToken);
            if (username != null) {
                UserDetails userDetails = this.userDetailsService
                        .loadUserByUsername(username);
                boolean validateToken = jwtTokenUtil.validateToken(authToken,
                        userDetails);
                if (validateToken) {
                    Authentication securityAuthentication = SecurityContextHolder
                            .getContext().getAuthentication();
                    if (securityAuthentication == null || JwtTokenUtil.ANONYMOUS_USER
                            .equals(securityAuthentication.getPrincipal())) {
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                userDetails, null,
                                userDetails.getAuthorities());
                        authentication
                                .setDetails(new WebAuthenticationDetailsSource()
                                        .buildDetails(request));
                        SecurityContextHolder.getContext()
                                .setAuthentication(authentication);
                    }
                    return super.preHandle(request, response, handler);
                }
            }
        }
        SecurityContextHolder.getContext().setAuthentication(null);
        // 返回需要登录信息
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.addHeader("aias-error", JwtTokenUtil.string2Unicode("请重新登录"));
        response.addHeader("aias-params", "need login");
        PrintWriter writer = response.getWriter();
        try {
            writer.flush();
        } finally {
            writer.close();
        }
        return false;
    }
}
