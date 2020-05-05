package mapper.api;

import model.dto.UserDto;
import model.entity.User;

public interface UserMapper {

     UserDto toDto(User user);
     User toUserFromDto(UserDto user);
}
