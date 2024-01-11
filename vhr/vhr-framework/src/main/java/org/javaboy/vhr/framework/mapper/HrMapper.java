package org.javaboy.vhr.framework.mapper;

import org.javaboy.vhr.framework.entity.Hr;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.javaboy.vhr.framework.entity.Role;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author javaboy
 * @since 2024-01-03
 */
public interface HrMapper extends BaseMapper<Hr> {

    List<Role> getHrRolesByHrId(Integer hrid);
}
