package az.spring.developer.service;

import az.spring.rest.model.request.AppointmentDto;
import az.spring.rest.model.request.UserDto;
import az.spring.rest.model.request.UserRegisterDto;
import az.spring.rest.model.response.UserResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {

    UserResponse getAllUsers();
    UserDto getUserById(Long id);
    ResponseEntity<String> createUser(UserRegisterDto userRegisterDto);
    ResponseEntity<String> updateUser(Long userId, UserRegisterDto userRegisterDto);
    ResponseEntity<String> deleteUSer(Long userId);


    List<AppointmentDto> getAppointmentsByUserId(Long id);
    ResponseEntity<String> addAppointmentToUser(Long id, AppointmentDto appointmentDto);
    ResponseEntity<String> updateAppointmentByUser(Long id,Long appointmentId,AppointmentDto appointmentDto);
    ResponseEntity<String> deleteUserAppointment(Long id, Long appointmentId);
}
