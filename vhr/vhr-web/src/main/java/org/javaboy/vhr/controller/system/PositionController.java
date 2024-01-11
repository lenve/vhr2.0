package org.javaboy.vhr.controller.system;

import org.javaboy.vhr.framework.entity.RespBean;
import org.javaboy.vhr.framework.entity.RespPageBean;
import org.javaboy.vhr.system.entity.Position;
import org.javaboy.vhr.system.service.IPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author：江南一点雨
 * @site：http://www.javaboy.org
 * @微信公众号：江南一点雨
 * @github：https://github.com/lenve
 * @gitee：https://gitee.com/lenve
 */
@RestController
@RequestMapping("/system/basic/position")
public class PositionController {

    @Autowired
    IPositionService positionService;

    @GetMapping("/{id}")
    public RespBean getPositionById(@PathVariable Integer id) {
        return RespBean.ok(null, positionService.getById(id));
    }
    @GetMapping
    public RespPageBean getPositionsByPage(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size) {
        return positionService.getPositionsByPage(page, size);
    }

    @PutMapping
    public RespBean updatePositionById(@RequestBody Position position) {
        return positionService.updateById(position) ? RespBean.ok("更新成功") : RespBean.error("更新失败");
    }

    @PostMapping
    public RespBean addPosition(@RequestBody Position position) {
        return positionService.addPosition(position);
    }

    @DeleteMapping("/{id}")
    public RespBean deletePositionById(@PathVariable Integer id) {
        return positionService.deletePositionById(id);
    }
}
