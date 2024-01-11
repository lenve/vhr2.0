package org.javaboy.vhr.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.javaboy.vhr.framework.entity.RespBean;
import org.javaboy.vhr.framework.entity.RespPageBean;
import org.javaboy.vhr.system.entity.Position;
import org.javaboy.vhr.system.mapper.PositionMapper;
import org.javaboy.vhr.system.service.IPositionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author javaboy
 * @since 2024-01-10
 */
@Service
public class PositionServiceImpl extends ServiceImpl<PositionMapper, Position> implements IPositionService {

    @Override
    public RespPageBean getPositionsByPage(Integer page, Integer size) {
        Page<Position> result = page(new Page<>(page, size));
        List<Position> records = result.getRecords();
        long total = result.getTotal();
        return new RespPageBean(total, records);
    }

    @Override
    public RespBean addPosition(Position position) {
        QueryWrapper<Position> qw = new QueryWrapper<>();
        qw.lambda().eq(Position::getName, position.getName());
        Position one = getOne(qw);
        if (one == null) {
            //没有重复的职位名
            position.setEnabled(true);
            position.setCreateDate(LocalDateTime.now());
            return save(position) ? RespBean.ok("添加成功") : RespBean.error("添加失败");
        }else{
            return RespBean.error("职位名重复，添加失败");
        }
    }

    @Override
    public RespBean deletePositionById(Integer id) {
        Position one = getById(id);
        if (one == null) {
            //要删除的数据不存在
            return RespBean.error("数据不存在，删除失败");
        }
        return removeById(id) ? RespBean.ok("删除成功") : RespBean.ok("删除失败");
    }
}
