package az.spring.developer.service.impl;

import az.spring.developer.exception.AppointmentNotFoundException;
import az.spring.developer.exception.ServiceProviderNotFoundException;
import az.spring.developer.exception.UserNotFoundException;
import az.spring.developer.mapper.AppoinmentMapper;
import az.spring.developer.model.Appointment;
import az.spring.developer.model.ServiceProvider;
import az.spring.developer.model.User;
import az.spring.developer.repository.AppointmentRepository;
import az.spring.developer.repository.ServiceProviderRepository;
import az.spring.developer.repository.UserRepository;
import az.spring.rest.model.request.AppointmentDto;
import az.spring.developer.service.AppointmentService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
@Getter
@Setter
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final AppoinmentMapper appointmentMapper;
    private final UserRepository userRepository;
    private final ServiceProviderRepository serviceProviderRepository;

    @Override
    public AppointmentDto getAppointmentById(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(()-> new AppointmentNotFoundException("Appointment not found with id:" + id));
        return appointmentMapper.toDto(appointment);
    }

    @Override
    public List<AppointmentDto> getAllAppointments() {
        List<Appointment> appointments = appointmentRepository.findAll();
        return appointments.stream()
                .map(appointmentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ResponseEntity<String> createAppointment(AppointmentDto appointmentDto) {
        User user = userRepository.findById(appointmentDto.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + appointmentDto.getUserId()));

        ServiceProvider serviceProvider = serviceProviderRepository.findById(appointmentDto.getServiceProviderId())
                .orElseThrow(() -> new ServiceProviderNotFoundException("ServiceProvider not found with id: " + appointmentDto.getServiceProviderId()));

        Appointment appointment = appointmentMapper.toEntity(appointmentDto);
        appointment.setUser(user);
        appointment.setServiceProvider(serviceProvider);
        appointment.setAppointmentDate(appointmentDto.getAppointmentDate());
        appointment.setStatus(Appointment.AppointmentStatus.PENDING);

        appointmentRepository.save(appointment);
        return ResponseEntity.status(HttpStatus.CREATED).body("Appointment created successfully");
    }

    @Override
    public ResponseEntity<String> updateAppointment(Long id, AppointmentDto appointmentDto) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(()-> new AppointmentNotFoundException("Appointment not found with id:" + id));

        if (appointmentDto.getUserId() != null){
            User user = userRepository.findById(appointmentDto.getUserId())
                    .orElseThrow(() -> new UserNotFoundException("User not found with id: " + appointmentDto.getUserId()));
            appointment.setUser(user);
        }
        if (appointmentDto.getServiceProviderId() != null){
            ServiceProvider provider = serviceProviderRepository.findById(appointmentDto.getServiceProviderId())
                    .orElseThrow(() -> new ServiceProviderNotFoundException("ServiceProvider not found with id:"
                    + appointmentDto.getServiceProviderId()));
            appointment.setServiceProvider(provider);
        }
        if (appointmentDto.getAppointmentDate() != null){
            appointment.setAppointmentDate(appointmentDto.getAppointmentDate());
        }
        if (appointmentDto.getStatus() != null){
            appointment.setStatus(appointmentDto.getStatus());
        }
        appointmentRepository.save(appointment);
        return ResponseEntity.ok("Appointment updated successfully");
    }

    @Override
    public ResponseEntity<String> deleteAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found with id:" + id));
        appointmentRepository.delete(appointment);
        return ResponseEntity.ok("Appointment deleted successfully");
    }
}
