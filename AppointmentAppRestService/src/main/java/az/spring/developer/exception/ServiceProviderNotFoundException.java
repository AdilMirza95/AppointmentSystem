package az.spring.developer.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServiceProviderNotFoundException extends RuntimeException{

    public ServiceProviderNotFoundException(String message){
        super(message);
    }
}
