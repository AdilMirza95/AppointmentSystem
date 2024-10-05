package az.spring.developer.controller;

import az.spring.rest.model.request.AppointmentDto;
import az.spring.rest.model.request.ServiceProviderDto;
import az.spring.rest.model.request.ServiceProviderRegisterDto;
import az.spring.rest.model.response.ServiceProviderResponse;
import az.spring.developer.service.ServiceProviderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("service-providers")
@RequiredArgsConstructor
public class ServiceProviderController {


    private final ServiceProviderService serviceProviderService;

    @GetMapping("/{id}")
    public ResponseEntity<ServiceProviderDto> getServiceProviderById(@PathVariable Long id){
        ServiceProviderDto serviceProviderDto = serviceProviderService.getServiceProviderById(id);
        return ResponseEntity.ok(serviceProviderDto);
    }

    @GetMapping("/all")
    public ResponseEntity<ServiceProviderResponse> getAllServiceProvider(){
        ServiceProviderResponse response = serviceProviderService.getAllServiceProviders();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    public ResponseEntity<String> createServiceProvider(@RequestBody ServiceProviderRegisterDto serviceProviderRegisterDto){
        return serviceProviderService.createServiceProvider(serviceProviderRegisterDto);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateServiceProvider(@PathVariable Long id,
                                                        @RequestBody ServiceProviderRegisterDto registerDto){
        return  serviceProviderService.updateServiceProvider(id,registerDto);
    }

    @DeleteMapping("delete/{serviceProviderId}")
    public ResponseEntity<String> deleteServiceProvider(@PathVariable Long serviceProviderId){
        return serviceProviderService.deleteServiceProvider(serviceProviderId);
    }

    @DeleteMapping("delete/Appointments/{serviceProviderId}")
    public ResponseEntity<String> deleteServiceProviderAndAppoinments(@PathVariable Long serviceProviderId){
        return serviceProviderService.deleteServiceProviderAndAppointment(serviceProviderId);
    }

    @GetMapping("/appointments/{serviceProviderId}")
    public ResponseEntity<List<AppointmentDto>> getAppointmentsByServiceProviderId(@PathVariable Long serviceProviderId){
        List<AppointmentDto> appointmentDtoList =  serviceProviderService.getAppointmentsByServiceProviderId(serviceProviderId);
        return ResponseEntity.ok(appointmentDtoList);
    }

    @PutMapping("/update-status/{serviceProviderId}/{appointmentId}")
    public ResponseEntity<String> updateAppointmentStatus(@PathVariable Long serviceProviderId,
                                                          @PathVariable Long appointmentId,
                                                          @RequestBody AppointmentDto appointmentDto){
        serviceProviderService.updateAppointmentStatus(serviceProviderId, appointmentId, appointmentDto);
        return ResponseEntity.ok("Appointment updated successfully");
    }

    @DeleteMapping("/appointments/{serviceProviderId}/{appointmentId}")
    public ResponseEntity<String> deleteServiceProviderAppointment(@PathVariable Long serviceProviderId,
                                                                   @PathVariable Long appointmentId){
        return serviceProviderService.deleteServiceProviderAppointment(serviceProviderId, appointmentId);
    }

}
