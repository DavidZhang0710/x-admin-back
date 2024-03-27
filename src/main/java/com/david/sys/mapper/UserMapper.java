package com.david.sys.mapper;

import com.david.sys.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author david
 * @since 2024-03-25
 */
public interface UserMapper extends BaseMapper<User> {
    public List<String> getRoleNameByUserId(Integer userId);
}
