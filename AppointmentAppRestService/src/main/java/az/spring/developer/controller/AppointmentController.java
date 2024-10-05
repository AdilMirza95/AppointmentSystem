package az.spring.developer.controller;

import az.spring.rest.model.request.AppointmentDto;
import az.spring.developer.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentDto> getAppointmentById(@PathVariable Long id){
         AppointmentDto appointmentDto = appointmentService.getAppointmentById(id);
         return ResponseEntity.ok(appointmentDto);
    }

    @GetMapping("/all")
    public ResponseEntity<List<AppointmentDto>> getAllAppointments(){
         List<AppointmentDto> appointments = appointmentService.getAllAppointments();
         return ResponseEntity.ok(appointments);
    }

    @PostMapping("/create")
    public ResponseEntity<String> addAppointment(@RequestBody AppointmentDto appointmentDto){
        return appointmentService.createAppointment(appointmentDto);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateAppoinment(@PathVariable("id") Long id,
                                                   @RequestBody AppointmentDto appointmentDto){
        return appointmentService.updateAppointment(id,appointmentDto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteAppointmentById(@PathVariable Long id){
        return appointmentService.deleteAppointment(id);
    }

}
