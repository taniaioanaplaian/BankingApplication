package mapper.impl;

import javafx.scene.control.Button;
import mapper.api.UserMapper;
import model.dto.UserDto;
import model.entity.User;
import service.api.UserService;

public class UserMapperImpl implements UserMapper {

    private UserService userService;
    public UserMapperImpl(UserService userService){
            this.userService = userService;
    }

    @Override
    public UserDto toDto(User user) {
        return new UserDto(user.getUserName(), user.getRole().toString(), new Button("Delete"));
    }

    @Override
    public User toUserFromDto(UserDto user) {

        User userC = userService.findByUsername(user.getUsername());
        return userC;
    }
}
