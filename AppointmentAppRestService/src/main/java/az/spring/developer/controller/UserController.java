package az.spring.developer.controller;

import az.spring.rest.model.request.AppointmentDto;
import az.spring.rest.model.request.UserDto;
import az.spring.rest.model.request.UserRegisterDto;
import az.spring.rest.model.response.UserResponse;
import az.spring.developer.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/all")
    public ResponseEntity<UserResponse> getAllUsers(){
         UserResponse response = userService.getAllUsers();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable("id") Long id){
        UserDto userDto =  userService.getUserById(id);
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/create")
    public ResponseEntity<String> addUser(@Valid @RequestBody UserRegisterDto userRegisterDto){
        return userService.createUser(userRegisterDto);
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<String> updateUser(@PathVariable Long userId,
                                             @RequestBody UserRegisterDto userRegisterDto){
        return userService.updateUser(userId, userRegisterDto);
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable("userId") Long userId){
        return userService.deleteUSer(userId);
    }


    @GetMapping("/appointments/{id}")
    public List<AppointmentDto> getAppointmentsByUserId(@PathVariable Long id) {
       return userService.getAppointmentsByUserId(id);

    }

    @PostMapping("/add-appointments/{id}")
    public ResponseEntity<String> addAppointmentToUser(@PathVariable ("id") Long id,
                                                       @RequestBody AppointmentDto appointmentDto) {
       return userService.addAppointmentToUser(id, appointmentDto);

    }

    @PutMapping("/appointments/update/{id}/{appointmentId}")
    public ResponseEntity<String> updateAppointmentByUser(@PathVariable Long id,
                                                          @PathVariable Long appointmentId,
                                                          @RequestBody AppointmentDto appointmentDto){
       return userService.updateAppointmentByUser(id, appointmentId, appointmentDto);
    }

    @DeleteMapping("/appointments/delete/{id}/{appointmentId}")
    public ResponseEntity<String> deleteAppointmentByUser(@PathVariable Long id,
                                                          @PathVariable Long appointmentId){
        return userService.deleteUserAppointment(id,appointmentId);
    }
}
