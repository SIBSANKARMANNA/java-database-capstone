import java.util.List;
import java.util.Map;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;





@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private TokenService tokenService;

    /* =========================
       BOOK APPOINTMENT
    ========================= */
    public int bookAppointment(Appointment appointment) {
        try {
            appointmentRepository.save(appointment);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    /* =========================
       UPDATE APPOINTMENT
    ========================= */
    public ResponseEntity<Map<String, String>> updateAppointment(Appointment appointment) {

        Optional<Appointment> existing =
                appointmentRepository.findById(appointment.getId());

        if (existing.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Appointment not found"));
        }

        appointmentRepository.save(appointment);
        return ResponseEntity.ok(Map.of("message", "Appointment updated"));
    }

    /* =========================
       CANCEL APPOINTMENT
    ========================= */
    public ResponseEntity<Map<String, String>> cancelAppointment(long id, String token) {

        Optional<Appointment> appointmentOpt =
                appointmentRepository.findById(id);

        if (appointmentOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Appointment not found"));
        }

        appointmentRepository.delete(appointmentOpt.get());
        return ResponseEntity.ok(Map.of("message", "Appointment cancelled"));
    }

    /* =========================
       GET APPOINTMENTS (DOCTOR)
    ========================= */
    public Map<String, Object> getAppointment(
            String pname, LocalDate date, String token) {

        String email = tokenService.extractIdentifier(token);
        Doctor doctor = doctorRepository.findByEmail(email);

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(LocalTime.MAX);

        List<Appointment> appointments;

        if (pname == null || pname.equals("null")) {
            appointments =
                    appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(
                            doctor.getId(), start, end);
        } else {
            appointments =
                    appointmentRepository
                            .findByDoctorIdAndPatient_NameContainingIgnoreCaseAndAppointmentTimeBetween(
                                    doctor.getId(), pname, start, end);
        }

        return Map.of("appointments", appointments);
    }
}
