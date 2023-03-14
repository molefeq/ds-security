package com.que.ads.ds.security.service;

import com.que.ads.ds.common.exception.UniqueFieldException;
import com.que.ads.ds.security.entity.User;
import com.que.ads.ds.security.mappers.UserMapper;
import com.que.ads.ds.security.models.ForgotPasswordModel;
import com.que.ads.ds.security.models.request.RegisterModel;
import com.que.ads.ds.security.models.request.ResetPasswordModel;
import com.que.ads.ds.security.models.response.RegisterResponseModel;
import com.que.ads.ds.security.models.security.SecurityUser;
import com.que.ads.ds.security.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public void checkUsername(String username) {
        var user = userRepository.findUserByEmailAddress(username);

        if (user.isEmpty()) {
            return;
        }


        var users = userRepository.findUsersStartingWithUsername(username);
        String suggestedUser = null;

        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            suggestedUser = String.format("%s%s", username, i);
            String finalSuggestedUser = suggestedUser;
            if (users.stream().filter(item -> finalSuggestedUser.equalsIgnoreCase(item.getUserName())).count() > 0) {
                continue;
            }
            break;
        }


        throw new UniqueFieldException(String.format("Username already exists pick a different username, the %s is available.", suggestedUser))
                .field("Username");
    }

    public void checkEmailAddress(String emailAddress) {
        var user = userRepository.findUserByEmailAddress(emailAddress);

        if (user.isPresent()) {
            throw new UniqueFieldException("Email address already exists pick a different email address")
                    .field("Email Address");
        }
    }

    public Optional<User> login(String username) {
        var user = userRepository.findUserByUsername(username);

        if (user.isPresent()) {
            userMapper.fromLoginModel(user.get());
            userRepository.save(user.get());
        }

        return user;
    }

    public RegisterResponseModel register(RegisterModel registerModel) {
        checkUsername(registerModel.getUsername());
        checkEmailAddress(registerModel.getEmailAddress());

        User registeredUser = userRepository.save(userMapper.fromRegisterModel(registerModel));

        return new RegisterResponseModel(String.format("Hi, %s %s you have successfully registers please please http://www.queads.co.za/Login to login.", registeredUser.getName(), registeredUser.getLastName()));
    }

    public ForgotPasswordModel forgotPassword(String username) throws UsernameNotFoundException {
        var user = userRepository.findUserByUsername(username);

        if (user.isEmpty()) {
            throw new UsernameNotFoundException(String.format("Username was not found %s", username));
        }

        return new ForgotPasswordModel();
    }

    public UserDetails resetPassword(ResetPasswordModel resetPasswordModel, String forgotPasswordKey) throws UsernameNotFoundException {
        var optionalUser = userRepository.findUserByForgotPasswordKey(forgotPasswordKey);

        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException(String.format("Entry not found."));
        }

        var user = optionalUser.get();

        if (resetPasswordModel.getUsername().equals(user.getUserName())) {
            throw new UsernameNotFoundException(String.format("Entry not found."));
        }

        userMapper.fromResetPasswordModel(resetPasswordModel, user);

        User resetUser = userRepository.save(user);

        return new SecurityUser(resetUser);
    }


}
