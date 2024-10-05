package az.spring.developer.service;

import az.spring.rest.model.request.AppointmentDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AppointmentService {

    AppointmentDto getAppointmentById(Long id);
    ResponseEntity<String> createAppointment(AppointmentDto appointmentDto);
    ResponseEntity<String> updateAppointment(Long id, AppointmentDto appointmentDto);
    ResponseEntity<String> deleteAppointment(Long id);
    List<AppointmentDto> getAllAppointments();
}
