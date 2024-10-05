package az.spring.developer.service.impl;

import az.spring.developer.exception.AppointmentNotFoundException;
import az.spring.developer.exception.ServiceProviderNotFoundException;
import az.spring.developer.exception.UnauthorizedException;
import az.spring.developer.exception.UserNotFoundException;
import az.spring.developer.mapper.AppoinmentMapper;
import az.spring.developer.mapper.ServiceProviderMapper;
import az.spring.developer.model.Appointment;
import az.spring.developer.model.ServiceProvider;
import az.spring.developer.model.User;
import az.spring.developer.repository.AppointmentRepository;
import az.spring.developer.repository.ServiceProviderRepository;
import az.spring.developer.repository.UserRepository;
import az.spring.rest.model.request.AppointmentDto;
import az.spring.rest.model.request.ServiceProviderDto;
import az.spring.rest.model.request.ServiceProviderRegisterDto;
import az.spring.rest.model.response.ServiceProviderResponse;
import az.spring.developer.service.AppointmentService;
import az.spring.developer.service.ServiceProviderService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Getter
@Setter
public class ServiceProviderServiceImpl implements ServiceProviderService {

    private final ServiceProviderRepository serviceProviderRepository;
    private final AppointmentService appointmentService;
    private final AppointmentRepository appointmentRepository;
    private final ServiceProviderMapper mapper;
    private final AppoinmentMapper appointmentMapper;
    private final UserRepository userRepository;

    @Override
    public ServiceProviderDto getServiceProviderById(Long id) {
        ServiceProvider serviceProvider = serviceProviderRepository.findById(id)
                .orElseThrow(() -> new ServiceProviderNotFoundException("ServiceProvider not found with id: " + id));
        return mapper.toDto(serviceProvider);
    }

    @Override
    public ServiceProviderResponse getAllServiceProviders() {
        List<ServiceProvider> serviceProviderList =  serviceProviderRepository.findAll();
        List<ServiceProviderDto> serviceProviderDtoList = serviceProviderList.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        return new ServiceProviderResponse(serviceProviderDtoList);
    }

    @Override
    public ResponseEntity<String> createServiceProvider(ServiceProviderRegisterDto serviceProviderRegisterDto) {
        User user = userRepository.findById(serviceProviderRegisterDto.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + serviceProviderRegisterDto.getUserId()));

       ServiceProvider serviceProvider = mapper.toEntity1(serviceProviderRegisterDto);
       serviceProvider.setUser(user);
       serviceProviderRepository.save(serviceProvider);
       return ResponseEntity.status(HttpStatus.CREATED).body("ServiceProvider created successfully");
    }

    @Override
    public ResponseEntity<String> updateServiceProvider(Long id, ServiceProviderRegisterDto registerDto) {

        ServiceProvider serviceProvider = serviceProviderRepository.findById(id)
                .orElseThrow(() -> new ServiceProviderNotFoundException("ServiceProvider not found with id: " + id));

        if (registerDto.getUserId() != null){
            User user = userRepository.findById(registerDto.getUserId())
                    .orElseThrow(() -> new UserNotFoundException("User not found with id: " + registerDto.getUserId()));
            serviceProvider.setUser(user);
        }
        if (registerDto.getName() != null){
            serviceProvider.setName(registerDto.getName());
        }
        if (registerDto.getServiceType() != null){
            serviceProvider.setServiceType(registerDto.getServiceType());
        }
        if (registerDto.getEmail() != null){
            serviceProvider.setEmail(registerDto.getEmail());
        }
        if (registerDto.getPassword() != null){
            serviceProvider.setPassword(registerDto.getPassword());
        }
        serviceProviderRepository.save(serviceProvider);

        return  ResponseEntity.ok("Service Provider updated successfully");
    }

    @Override
    @Transactional
    public ResponseEntity<String> deleteServiceProvider(Long serviceProviderId) {
        List<Appointment> appointments = appointmentRepository.findByServiceProviderId(serviceProviderId);
        for (Appointment appointment : appointments){
             appointment.setServiceProvider(null);
             appointment.setStatus(Appointment.AppointmentStatus.CANCELLED);
            appointmentRepository.save(appointment);
        }
        serviceProviderRepository.deleteById(serviceProviderId);
        return ResponseEntity.ok("ServiceProvider deleted successfully");
    }

    @Override
    public ResponseEntity<String> deleteServiceProviderAndAppointment(Long serviceProviderId) {
        List<Appointment> appointments = appointmentRepository.findByServiceProviderId(serviceProviderId);
        appointmentRepository.deleteAll(appointments);
        serviceProviderRepository.deleteById(serviceProviderId);
        return ResponseEntity.ok("ServiceProvider deleted successfully");
    }


    @Override
    public List<AppointmentDto> getAppointmentsByServiceProviderId(Long serviceProviderId) {
        List<Appointment> appointments = appointmentRepository.findByServiceProviderId(serviceProviderId);

        return appointments.stream()
                .map(appointmentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void updateAppointmentStatus(Long serviceProviderId, Long appointmentId,AppointmentDto appointmentDto) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(()-> new AppointmentNotFoundException("Appointment not found with id:" + appointmentId));

        if (appointment.getServiceProvider() == null || !appointment.getServiceProvider().getId().equals(serviceProviderId)) {
            throw new UnauthorizedException("Service Provider is not authorized to update this appointment");
        }
        appointment.setStatus(appointmentDto.getStatus());
        appointmentRepository.save(appointment);
    }

    @Override
    public ResponseEntity<String> deleteServiceProviderAppointment(Long serviceProviderId, Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(()-> new AppointmentNotFoundException("Appointment not found with id:" + serviceProviderId));

        if (!appointment.getServiceProvider().getId().equals(serviceProviderId)){
            throw new UnauthorizedException("ServiceProvider is not authorized to delete this appointment");
        }
        appointmentService.deleteAppointment(appointmentId);
        return ResponseEntity.ok("Appointment deleted successfully");
    }

}
