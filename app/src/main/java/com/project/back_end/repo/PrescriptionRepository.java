import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.mongodb.repository.MongoRepository;




public interface PrescriptionRepository extends MongoRepository<Prescription, String> {

    List<Prescription> findByAppointmentId(Long appointmentId);
}
