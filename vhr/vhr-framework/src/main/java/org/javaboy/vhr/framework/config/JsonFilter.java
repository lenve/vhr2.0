package org.javaboy.vhr.framework.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.javaboy.vhr.framework.entity.Hr;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

/**
 * @author：江南一点雨
 * @site：http://www.javaboy.org
 * @微信公众号：江南一点雨
 * @github：https://github.com/lenve
 * @gitee：https://gitee.com/lenve
 */
public class JsonFilter extends UsernamePasswordAuthenticationFilter {
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        String contentType = request.getContentType();
        if (contentType.equalsIgnoreCase(MediaType.APPLICATION_JSON_VALUE) || contentType.equalsIgnoreCase(MediaType.APPLICATION_JSON_UTF8_VALUE)) {
            //认为前端传来的是 JSON 格式的参数
            try {
                //通过 IO 流的形式去解析请求体中的参数
                Hr hr = new ObjectMapper().readValue(request.getInputStream(), Hr.class);
                String username = hr.getUsername();
                username = (username != null) ? username.trim() : "";
                String password = hr.getPassword();
                password = (password != null) ? password : "";
                UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(username,
                        password);
                // Allow subclasses to set the "details" property
                setDetails(request, authRequest);
                //获取认证管理器去认证
                return this.getAuthenticationManager().authenticate(authRequest);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            //认为前端传来的是 key-value 格式的参数
            return super.attemptAuthentication(request, response);
        }
    }
}
