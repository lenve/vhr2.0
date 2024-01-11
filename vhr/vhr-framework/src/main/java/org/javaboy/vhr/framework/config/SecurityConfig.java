package org.javaboy.vhr.framework.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.javaboy.vhr.framework.entity.Hr;
import org.javaboy.vhr.framework.entity.RespBean;
import org.javaboy.vhr.framework.entity.Role;
import org.javaboy.vhr.framework.entity.vo.MenuWithRole;
import org.javaboy.vhr.framework.service.IHrService;
import org.javaboy.vhr.framework.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.util.AntPathMatcher;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author：江南一点雨
 * @site：http://www.javaboy.org
 * @微信公众号：江南一点雨
 * @github：https://github.com/lenve
 * @gitee：https://gitee.com/lenve
 */
@Configuration
public class SecurityConfig {

    @Autowired
    IHrService hrService;
    @Autowired
    IMenuService menuService;
    AntPathMatcher antPathMatcher = new AntPathMatcher();

    /**
     * 当我们登录成功之后，Security 会自动将用户信息存入到两个地方：
     * 1. HttpSession
     * 2. SecurityContextHolder：这个本质上是一个 ThreadLocal，这个的特点是在哪个线程中存入的数据，就在哪个线程读取，换一个线程就读取不出来了。
     * 一般的流程是这样：
     * 登录成功之后，虽然信息存入到了 SecurityContextHolder 中，在 Spring Security 后续的执行流程中，凡是需要获取当前用户信息的时候，都从 SecurityContextHolder 中获取，而不会
     * 从 HttpSession 获取（因为并非所有系统都会选择把信息存入到 HttpSession，例如有时候是 JWT 登录，信息可能存在 Redis 中），当登录请求结束的时候，会将 SecurityContextHolder 中
     * 的信息清除。下一个请求到达的时候，会先从 HttpSession 中读取登录用户信息，然后存入到 SecurityContextHolder 中，这样在后续的流程中，就可以使用 SecurityContextHolder 中的信息了
     *
     * @return
     */
    @Bean
    JsonFilter jsonFilter() {
        JsonFilter jsonFilter = new JsonFilter();
        //登录地址
        jsonFilter.setFilterProcessesUrl("/login");
        jsonFilter.setAuthenticationSuccessHandler((req, resp, auth) -> {
            resp.setContentType("application/json;charset=utf-8");
            Hr principal = (Hr) auth.getPrincipal();
            principal.setPassword(null);
            resp.getWriter().write(new ObjectMapper().writeValueAsString(RespBean.ok("登录成功", principal)));
        });
        jsonFilter.setAuthenticationFailureHandler((req, resp, e) -> {
            resp.setContentType("application/json;charset=utf-8");
            RespBean error = RespBean.error("登录失败");
            if (e instanceof BadCredentialsException) {
                error.setMessage("用户名或者密码输入错误，登录失败");
            } else if (e instanceof DisabledException) {
                error.setMessage("账户被禁用，登录失败");
            } else if (e instanceof LockedException) {
                error.setMessage("账户被锁定，登录失败");
            } else if (e instanceof AccountExpiredException) {
                error.setMessage("账户过期，登录失败");
            } else if (e instanceof CredentialsExpiredException) {
                error.setMessage("密码过期，登录失败");
            }
            resp.getWriter().write(new ObjectMapper().writeValueAsString(error));
        });
        jsonFilter.setAuthenticationManager(authenticationManager());
        jsonFilter.setSecurityContextRepository(new HttpSessionSecurityContextRepository());
        return jsonFilter;
    }

    @Bean
    AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(hrService);
        ProviderManager providerManager = new ProviderManager(daoAuthenticationProvider);
        return providerManager;
    }

    /**
     * 这个就是 Spring Security 过滤器链，Spring Security 中的所有功能都是通过这个链来提供的
     *
     * @return
     */
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //这个就是拦截所有请求，但是不经过任何过滤器
//        return new DefaultSecurityFilterChain(new AntPathRequestMatcher("/**"));
        http.authorizeHttpRequests(p -> p.anyRequest().access((authentication, object) -> {
                    boolean granted = false;
                    boolean isMatch = false;
                    //1. 根据当前请求分析出来当前请求属于 menu 中的哪一种 http://localhost:8080/personnel/ec/hello（menu）
                    //1.1 获取当前请求 url 地址
                    String requestURI = object.getRequest().getRequestURI();
                    //1.2 和 menu 表中的记录进行比较
                    List<MenuWithRole> menuWithRoles = menuService.getAllMenusWithRole();
                    Authentication auth = authentication.get();
                    for (MenuWithRole menuWithRole : menuWithRoles) {
                        if (antPathMatcher.match(menuWithRole.getUrl(), requestURI)) {
                            isMatch = true;
                            //如果匹配上了，说明当前请求的菜单就找到了
                            //2. 根据第一步分析的结果，进而分析出来当前 menu 需要哪些角色才能访问（menu_role）
                            List<Role> roles = menuWithRole.getRoles();
                            //3. 判断当前用户是否具备本次请求所需要的角色
                            //获取当前登录用户所具备的角色
                            Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
                            for (GrantedAuthority authority : authorities) {
                                for (Role role : roles) {
                                    if (authority.getAuthority().equals(role.getName())) {
                                        //说明当前用户具备所需要的角色
                                        granted = true;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    if (!isMatch) {
                        //也有可能 for 循环结束了，但是和数据库中的任何一项都没有匹配上
                        //例如 http://localhost:8080/menus
                        //此时，判断用户是否已经登录，如果登录，则允许访问，否则不允许
                        if (auth instanceof UsernamePasswordAuthenticationToken) {
                            //说明用户登录了
                            granted = true;
                        }
                    }
                    //如果 granted 为 true，表示请求通过；granted 为 false 表示请求不通过（即用户权限不足）
                    return new AuthorizationDecision(granted);
                }))
                .formLogin(f -> f.usernameParameter("username")
                        .passwordParameter("password")
                        .loginProcessingUrl("/login")
                        .successHandler((req, resp, auth) -> {
                            resp.setContentType("application/json;charset=utf-8");
                            Hr principal = (Hr) auth.getPrincipal();
                            principal.setPassword(null);
                            resp.getWriter().write(new ObjectMapper().writeValueAsString(RespBean.ok("登录成功", principal)));
                        }).failureHandler((req, resp, e) -> {
                            resp.setContentType("application/json;charset=utf-8");
                            RespBean error = RespBean.error("登录失败");
                            if (e instanceof BadCredentialsException) {
                                error.setMessage("用户名或者密码输入错误，登录失败");
                            } else if (e instanceof DisabledException) {
                                error.setMessage("账户被禁用，登录失败");
                            } else if (e instanceof LockedException) {
                                error.setMessage("账户被锁定，登录失败");
                            } else if (e instanceof AccountExpiredException) {
                                error.setMessage("账户过期，登录失败");
                            } else if (e instanceof CredentialsExpiredException) {
                                error.setMessage("密码过期，登录失败");
                            }
                            resp.getWriter().write(new ObjectMapper().writeValueAsString(error));
                        }))
                .csrf(c -> c.disable())
                .exceptionHandling(e -> e.authenticationEntryPoint((req, resp, ex) -> {
                    resp.setContentType("application/json;charset=utf-8");
                    resp.setStatus(401);
                    RespBean error = RespBean.error("尚未登录，请先登录");
                    resp.getWriter().write(new ObjectMapper().writeValueAsString(error));
                }))
                .logout(logout -> {
                    logout
                            //注销地址
                            .logoutUrl("/logout")
                            .logoutSuccessHandler((req, resp, auth) -> {
                                resp.setContentType("application/json;charset=utf-8");
                                Hr principal = (Hr) auth.getPrincipal();
                                principal.setPassword(null);
                                resp.getWriter().write(new ObjectMapper().writeValueAsString(RespBean.ok("注销成功", principal)));
                            });
                });
        //这个实际上就是在默认的过滤器基础之上进行构建
        http.addFilterBefore(jsonFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
