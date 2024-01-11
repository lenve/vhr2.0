package org.javaboy.vhr.system.service;

import org.javaboy.vhr.framework.entity.RespBean;
import org.javaboy.vhr.framework.entity.RespPageBean;
import org.javaboy.vhr.system.entity.Position;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author javaboy
 * @since 2024-01-10
 */
public interface IPositionService extends IService<Position> {

    RespPageBean getPositionsByPage(Integer page, Integer size);

    RespBean addPosition(Position position);

    RespBean deletePositionById(Integer id);
}
