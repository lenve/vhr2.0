package org.javaboy.vhr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author：江南一点雨
 * @site：http://www.javaboy.org
 * @微信公众号：江南一点雨
 * @github：https://github.com/lenve
 * @gitee：https://gitee.com/lenve
 */
@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "hello vhr";
    }

    @GetMapping("/employee/advanced/hello")
    public String hello3() {
        return "/employee/advanced/**";
    }

    @GetMapping("/employee/basic/hello")
    public String hello2() {
        return "/employee/basic/**";
    }
}
