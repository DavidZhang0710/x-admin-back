package com.david.sys.service;

import com.david.sys.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author david
 * @since 2024-03-25
 */
public interface IUserService extends IService<User> {

    Map<String, Object> login(User user);
}
