package az.spring.developer.service.impl;

import az.spring.developer.exception.AppointmentNotFoundException;
import az.spring.developer.exception.UnauthorizedException;
import az.spring.developer.exception.UserListEmptyException;
import az.spring.developer.exception.UserNotFoundException;
import az.spring.developer.mapper.AppoinmentMapper;
import az.spring.developer.mapper.UserMapper;
import az.spring.developer.model.Appointment;
import az.spring.developer.model.ServiceProvider;
import az.spring.developer.model.User;
import az.spring.developer.repository.AppointmentRepository;
import az.spring.developer.repository.ServiceProviderRepository;
import az.spring.developer.repository.UserRepository;
import az.spring.rest.model.request.AppointmentDto;
import az.spring.rest.model.request.UserDto;
import az.spring.rest.model.request.UserRegisterDto;
import az.spring.rest.model.response.UserResponse;
import az.spring.developer.service.AppointmentService;
import az.spring.developer.service.ServiceProviderService;
import az.spring.developer.service.UserService;
import jakarta.transaction.Transactional;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper mapper;
    private final AppointmentRepository appointmentRepository;
    private final AppoinmentMapper appointmentMapper;
    private final AppointmentService appointmentService;
    private final ServiceProviderRepository serviceProviderRepository;
    private final ServiceProviderService serviceProviderService;

    @Override
    public UserResponse getAllUsers() {
        List<User> userList = userRepository.findAll();
        if (userList.isEmpty()) {
            throw new UserListEmptyException();
        }
        List<UserDto> userDtoList = userList.stream()
                .map(mapper::toUserDto)
                .collect(Collectors.toList());

        return new UserResponse(userDtoList);
    }

    @Override
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        return mapper.toUserDto(user);
    }

    @Override
    public ResponseEntity<String> createUser(UserRegisterDto userRegisterDto) {
        if (userRegisterDto == null) {
            throw new IllegalArgumentException("Full name cannot be null or empty");
        }
        User user = mapper.toEntityFromUserRegisterDto(userRegisterDto);
        userRepository.save(user);

         return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully");
    }

    @Override
    public ResponseEntity<String> updateUser(Long userId, UserRegisterDto userRegisterDto) {
        User user = userRepository.findById(userId).
                orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        if (userRegisterDto.getFullName() != null){
            user.setFullName(userRegisterDto.getFullName());
        }
        if (userRegisterDto.getEmail() != null){
            user.setEmail(userRegisterDto.getEmail());
        }
        if (userRegisterDto.getPassword() != null){
            user.setPassword(userRegisterDto.getPassword());
        }
        userRepository.save(user);
        return ResponseEntity.ok("User updated successfully");
    }


    @Override
    @Transactional
    public ResponseEntity<String> deleteUSer(Long userId) {
        if (userId == null) {
            return ResponseEntity.badRequest().body("Invalid userID");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException( "User not found"));

        List<ServiceProvider> serviceProviders = serviceProviderRepository.findByUserId(userId);
        if (!serviceProviders.isEmpty()){
            ServiceProvider serviceProvider = serviceProviders.get(0);
            serviceProviderService.deleteServiceProviderAndAppointment(serviceProvider.getId());
        }
        userRepository.deleteById(userId);
        return ResponseEntity.ok("User deleted successfully");
    }

    public List<AppointmentDto>  getAppointmentsByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new UserNotFoundException("User not found with id: " + userId));
        List<Appointment> appointmentList = user.getAppointments();
        return appointmentList.stream()
                .map(appointmentMapper::toDto)
                .collect(Collectors.toList());
    }

    public ResponseEntity<String> addAppointmentToUser(Long id, AppointmentDto appointmentDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        Appointment appointment = appointmentMapper.toEntity(appointmentDto);
        appointment.setUser(user);

        user.getAppointments().add(appointment);
        userRepository.save(user);
        return ResponseEntity.ok("Appointment added to user successfully");
    }

    @Override
    public ResponseEntity<String>  updateAppointmentByUser(Long id, Long appointmentId, AppointmentDto appointmentDto) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(()-> new AppointmentNotFoundException("Appointment not found with id: " + appointmentId));

        if (!appointment.getUser().getId().equals(id)){
            throw new UnauthorizedException("User is not authorized to update this appointment");
        }
        AppointmentDto userUpdateDto = new AppointmentDto();
        userUpdateDto.setAppointmentDate(appointmentDto.getAppointmentDate());
        userUpdateDto.setServiceProviderId(appointmentDto.getServiceProviderId());

        appointmentService.updateAppointment(appointmentId,userUpdateDto);
        return ResponseEntity.ok("Appointment updated successfully");
    }

    @Override
    public ResponseEntity<String> deleteUserAppointment(Long id, Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found with id: " + appointmentId));

        if (!appointment.getUser().getId().equals(id)){
            throw new UnauthorizedException("User is not authorized to delete this appointment");
        }
        appointmentService.deleteAppointment(appointmentId);

        return ResponseEntity.ok("Appointment deleted successfully");
    }
}
