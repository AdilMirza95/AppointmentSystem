package az.spring.developer.mapper;

import az.spring.developer.model.Appointment;
import az.spring.developer.model.ServiceProvider;
import az.spring.developer.model.User;
import az.spring.rest.model.request.AppointmentDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface AppoinmentMapper {


    @Mapping(target = "user", source = "userId", qualifiedByName = "toUser")
    @Mapping(target = "serviceProvider", source = "serviceProviderId", qualifiedByName = "toServiceProvider")
    Appointment toEntity(AppointmentDto appointmentDto);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "serviceProviderId", source = "serviceProvider.id")
    AppointmentDto toDto(Appointment appointment);

    @Named("toUser")
    default User toUser(Long userId) {
        if (userId == null) {
            return null;
        }
        User user = new User();
        user.setId(userId);
        return user;
    }

    @Named("toServiceProvider")
    default ServiceProvider toServiceProvider(Long serviceProviderId) {
        if (serviceProviderId == null) {
            return null;
        }
        ServiceProvider serviceProvider = new ServiceProvider();
        serviceProvider.setId(serviceProviderId);
        return serviceProvider;
    }


}
