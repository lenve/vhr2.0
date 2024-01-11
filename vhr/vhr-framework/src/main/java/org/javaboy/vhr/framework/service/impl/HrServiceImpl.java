package org.javaboy.vhr.framework.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.javaboy.vhr.framework.entity.Hr;
import org.javaboy.vhr.framework.mapper.HrMapper;
import org.javaboy.vhr.framework.service.IHrService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author javaboy
 * @since 2024-01-03
 */
@Service
public class HrServiceImpl extends ServiceImpl<HrMapper, Hr> implements IHrService {

    @Autowired
    HrMapper hrMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        QueryWrapper<Hr> qw = new QueryWrapper<>();
        qw.lambda().eq(Hr::getUsername, username);
        //查询某一个具体的用户
        Hr hr = getOne(qw);
        if (hr == null) {
            throw new UsernameNotFoundException("用户不存在，登录失败");
        }
        hr.setRoles(hrMapper.getHrRolesByHrId(hr.getId()));
        return hr;
    }
}
