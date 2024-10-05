package az.spring.rest.model.response;

import az.spring.rest.model.request.ServiceProviderDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceProviderResponse {

    List<ServiceProviderDto> serviceProviders;
}
