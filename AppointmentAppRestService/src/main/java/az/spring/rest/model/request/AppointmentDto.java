package az.spring.rest.model.request;

import az.spring.developer.model.Appointment;
import lombok.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class AppointmentDto {

    private Long id;
    private Long userId;
    private Long serviceProviderId;
    private LocalDateTime appointmentDate;

    @NotNull
    private Appointment.AppointmentStatus status;
}
