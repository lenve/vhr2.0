package org.javaboy.vhr.framework.service;

import org.javaboy.vhr.framework.entity.Menu;
import com.baomidou.mybatisplus.extension.service.IService;
import org.javaboy.vhr.framework.entity.RespBean;
import org.javaboy.vhr.framework.entity.vo.MenuWithRole;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author javaboy
 * @since 2024-01-03
 */
public interface IMenuService extends IService<Menu> {

    RespBean getMenusByHrId();

    List<MenuWithRole> getAllMenusWithRole();
}
