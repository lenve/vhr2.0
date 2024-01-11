package org.javaboy.vhr.framework.entity.vo;

import org.javaboy.vhr.framework.entity.Menu;

import java.util.List;

/**
 * @author：江南一点雨
 * @site：http://www.javaboy.org
 * @微信公众号：江南一点雨
 * @github：https://github.com/lenve
 * @gitee：https://gitee.com/lenve
 */
public class MenuVO extends Menu {
    private List<MenuVO> children;

    public List<MenuVO> getChildren() {
        return children;
    }

    public void setChildren(List<MenuVO> children) {
        this.children = children;
    }
}
