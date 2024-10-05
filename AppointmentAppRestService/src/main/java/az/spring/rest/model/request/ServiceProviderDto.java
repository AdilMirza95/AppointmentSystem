package az.spring.rest.model.request;

import az.spring.developer.model.User;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class ServiceProviderDto {

    private Long id;

    @NotBlank(message = "name cannot be blank")
    private String name;

    private String serviceType;

    @NotBlank(message = "Email cannot be blank")
    @Column(unique = true)
    private String email;

    private Long userId;
}
