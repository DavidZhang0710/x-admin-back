package com.david.sys.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.david.sys.entity.User;
import com.david.sys.mapper.UserMapper;
import com.david.sys.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author david
 * @since 2024-03-25
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public Map<String, Object> login(User user) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, user.getUsername());
        User loginUser = this.baseMapper.selectOne(wrapper);
        if(loginUser != null && passwordEncoder.matches(user.getPassword(), loginUser.getPassword())) {
            String key = "user:" + UUID.randomUUID();
            loginUser.setPassword(null);
            redisTemplate.opsForValue().set(key, loginUser, 30, TimeUnit.MINUTES);
            Map<String, Object> map = new HashMap<>();
            map.put("token", key);
            return map;
        }
        return null;
    }

    @Override
    public Map<String, Object> getUserInfo(String token) {
        Object result = redisTemplate.opsForValue().get(token);
        if(result != null) {
            User loginUser = JSON.parseObject(JSON.toJSONString(result), User.class);
            Map<String, Object> map = new HashMap<>();
            map.put("name", loginUser.getUsername());
            map.put("avatar", loginUser.getAvatar());
            //查询角色
            map.put("roles", this.baseMapper.getRoleNameByUserId(loginUser.getId()));
            return map;
        }
        return null;
    }

    @Override
    public void logout(String token) {
        redisTemplate.delete(token);
    }
}
