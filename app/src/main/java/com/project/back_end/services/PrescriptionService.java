import java.util.List;
import java.util.Map;
import java.time.LocalDate;
import java.time.LocalDateTime;





@Service
public class PrescriptionService {

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    public ResponseEntity<Map<String, String>> savePrescription(
            Prescription prescription) {

        try {
            prescriptionRepository.save(prescription);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "Prescription saved"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error saving prescription"));
        }
    }

    public ResponseEntity<Map<String, Object>> getPrescription(
            Long appointmentId) {

        List<Prescription> prescriptions =
                prescriptionRepository.findByAppointmentId(appointmentId);

        return ResponseEntity.ok(Map.of("prescriptions", prescriptions));
    }
}
