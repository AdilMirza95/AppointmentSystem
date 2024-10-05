package az.spring.developer.repository;

import az.spring.developer.model.Appointment;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment,Long> {

    @Modifying
    @Transactional
    @Query("DELETE FROM Appointment a WHERE a.serviceProvider.id = :serviceProviderId")
    void deleteByServiceProviderId(@Param("serviceProviderId") Long serviceProviderId);

    List<Appointment> findByUserId(Long userId);
    List<Appointment> findByServiceProviderId(Long serviceProviderId);
}
