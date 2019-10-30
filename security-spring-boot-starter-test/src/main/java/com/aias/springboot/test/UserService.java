package com.aias.springboot.test;

import com.aias.springboot.security.jwt.JwtUserFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;

/**
 * <b>
 *
 * <br>
 * <b>@ClassName:</b> UserService <br>
 * <b>@Date:</b> 2019/10/29 <br>
 *
 * @author <a> liuhy </a><br>
 */
@Service
public class UserService implements UserDetailsService {
    public User selectById(long parseLong) {
        User user = new User();
        user.setId(1L);
        user.setPhone("18931461268");
        user.setRole(1L);
        user.setCreateTime(new Date());
        user.setUsername("zhangsan");
        return user;

    }

    public User queryByName(String phone) {
        User user = new User();
        user.setId(1L);
        user.setPhone("18931461268");
        user.setRole(1L);
        user.setCreateTime(new Date());
        user.setUsername("zhangsan");
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String name)
            throws UsernameNotFoundException {
        User userDTO = selectById(Long.parseLong(name));
        if (userDTO == null) {
            throw new UsernameNotFoundException(
                    String.format("No user found with username '%s'.", name));
        } else {
            return JwtUserFactory.create(String.valueOf(userDTO.getId()), userDTO.getPhone(), userDTO.getUsername(), Arrays.asList(String.valueOf(userDTO.getRole())));
        }
    }
}
