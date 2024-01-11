package org.javaboy.vhr.framework.mapper;

import org.javaboy.vhr.framework.entity.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.javaboy.vhr.framework.entity.vo.MenuVO;
import org.javaboy.vhr.framework.entity.vo.MenuWithRole;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author javaboy
 * @since 2024-01-03
 */
public interface MenuMapper extends BaseMapper<Menu> {

    List<MenuVO> getMenusByHrId(Integer hrid);

    List<MenuWithRole> getAllMenusWithRole();
}
