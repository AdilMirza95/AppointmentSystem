package az.spring.developer.service;

import az.spring.rest.model.request.AppointmentDto;
import az.spring.rest.model.request.ServiceProviderDto;
import az.spring.rest.model.request.ServiceProviderRegisterDto;
import az.spring.rest.model.response.ServiceProviderResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ServiceProviderService {

    ServiceProviderDto getServiceProviderById(Long id);
    ServiceProviderResponse getAllServiceProviders();
    ResponseEntity<String> createServiceProvider(ServiceProviderRegisterDto serviceProviderRegisterDto);
    ResponseEntity<String> updateServiceProvider(Long id, ServiceProviderRegisterDto registerDto);
    ResponseEntity<String> deleteServiceProvider(Long serviceProviderId);
    ResponseEntity<String> deleteServiceProviderAndAppointment(Long serviceProviderId);
    List<AppointmentDto> getAppointmentsByServiceProviderId(Long serviceProviderId);
    //void updateAppointmentStatus(Long serviceProviderId, Long appointmentId, Appointment.AppointmentStatus status);
    void updateAppointmentStatus(Long serviceProviderId,Long appointmentId,AppointmentDto appointmentDto);

    ResponseEntity<String> deleteServiceProviderAppointment(Long serviceProviderId, Long appointmentId);

}
