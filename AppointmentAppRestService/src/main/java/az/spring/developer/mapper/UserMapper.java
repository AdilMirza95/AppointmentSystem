package az.spring.developer.mapper;

import az.spring.developer.model.User;
import az.spring.rest.model.request.UserDto;
import az.spring.rest.model.request.UserRegisterDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {


    UserDto toUserDto(User user);

    UserRegisterDto toUserRegisterDto(User user);

    @Mapping(target = "id", ignore = true)
    User toEntityFromUserRegisterDto(UserRegisterDto userRegisterDto);

    @Mapping(target = "password", ignore = true)
    User toEntityFromUserDto(UserDto userDto);

}
