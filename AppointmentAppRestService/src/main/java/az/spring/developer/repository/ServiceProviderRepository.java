package az.spring.developer.repository;

import az.spring.developer.model.Appointment;
import az.spring.developer.model.ServiceProvider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceProviderRepository extends JpaRepository<ServiceProvider,Long> {

    List<ServiceProvider> findByUserId(Long userId);
}
