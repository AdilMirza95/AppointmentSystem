package az.spring.developer.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserListEmptyException extends  RuntimeException{

    public UserListEmptyException() {

        super("User list is empty");
    }

    public UserListEmptyException(String message) {
        super(message);
    }

}
