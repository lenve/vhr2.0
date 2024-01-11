package org.javaboy.vhr.framework.service;

import org.javaboy.vhr.framework.entity.Hr;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author javaboy
 * @since 2024-01-03
 */
public interface IHrService extends IService<Hr>, UserDetailsService {

}
