package az.spring.developer.mapper;

import az.spring.developer.model.ServiceProvider;
import az.spring.developer.model.User;
import az.spring.rest.model.request.ServiceProviderDto;
import az.spring.rest.model.request.ServiceProviderRegisterDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ServiceProviderMapper {

    @Mapping(target = "user", source = "userId", qualifiedByName = "toUser")
    ServiceProvider toEntity(ServiceProviderDto serviceProviderDto);

    @Mapping(target = "user",source = "userId",qualifiedByName = "toUser")
    ServiceProvider toEntity1(ServiceProviderRegisterDto serviceProviderRegisterDto);

    @Mapping(target = "userId", source = "user.id")
    ServiceProviderDto toDto(ServiceProvider serviceProvider);

    @Named("toUser")
    default User toUser(Long userId){
        if (userId == null) {
            return null;
        }
        User user = new User();
        user.setId(userId);
        return user;
    }
}
