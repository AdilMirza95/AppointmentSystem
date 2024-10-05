package az.spring.developer.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppointmentNotFoundException extends RuntimeException {

    public AppointmentNotFoundException (String message){
        super(message);
    }

}
