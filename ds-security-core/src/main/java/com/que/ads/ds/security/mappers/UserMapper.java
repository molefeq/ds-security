package com.que.ads.ds.security.mappers;

import com.que.ads.ds.common.utils.SqlTimestampUtil;
import com.que.ads.ds.security.entity.Role;
import com.que.ads.ds.security.entity.User;
import com.que.ads.ds.security.models.request.RegisterModel;
import com.que.ads.ds.security.models.request.ResetPasswordModel;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserMapper {
    private final PasswordEncoder passwordEncoder;

    public User fromRegisterModel(RegisterModel registerModel) {
        User user = new User();

        user.setName(registerModel.getName());
        user.setLastName(registerModel.getLastName());
        user.setUserName(registerModel.getUsername());
        user.setPassword(passwordEncoder.encode(registerModel.getPassword()));
        user.setEmailAddress(registerModel.getEmailAddress());
        user.setRole(toRole(registerModel.getRoleId()));
        user.setCreateDate(SqlTimestampUtil.now());
        user.setUserName(registerModel.getUsername());

        return user;
    }

    public void fromResetPasswordModel(ResetPasswordModel resetPasswordModel, User user) {
        user.setPassword(passwordEncoder.encode(resetPasswordModel.getPassword()));
        user.setForgotPasswordKey(null);
        user.setLastLoginDate(SqlTimestampUtil.now());
    }

    public void fromLoginModel(User user) {
        user.setLastLoginDate(SqlTimestampUtil.now());
    }

    private Role toRole(int roleId){
        Role role = new Role();

        role.setId(roleId);

        return role;
    }
}
