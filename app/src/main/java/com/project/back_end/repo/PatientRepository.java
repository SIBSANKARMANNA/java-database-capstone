import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;



public interface PatientRepository extends JpaRepository<Patient, Long> {

    Patient findByEmail(String email);

    Patient findByEmailOrPhone(String email, String phone);
}
