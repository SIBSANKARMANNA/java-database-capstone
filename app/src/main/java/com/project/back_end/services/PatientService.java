import java.util.List;
import java.util.Map;
import java.time.LocalDate;
import java.time.LocalDateTime;





@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private TokenService tokenService;

    /* =========================
       CREATE PATIENT
    ========================= */
    public int createPatient(Patient patient) {
        try {
            patientRepository.save(patient);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    /* =========================
       GET PATIENT APPOINTMENTS
    ========================= */
    public ResponseEntity<Map<String, Object>> getPatientAppointment(
            Long id, String token) {

        String email = tokenService.extractIdentifier(token);
        Patient patient = patientRepository.findByEmail(email);

        if (!patient.getId().equals(id)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<AppointmentDTO> dtos =
                appointmentRepository.findByPatientId(id)
                        .stream()
                        .map(AppointmentDTO::new)
                        .toList();

        return ResponseEntity.ok(Map.of("appointments", dtos));
    }

    /* =========================
       FILTER BY CONDITION
    ========================= */
    public ResponseEntity<Map<String, Object>> filterByCondition(
            String condition, Long id) {

        int status = condition.equalsIgnoreCase("past") ? 1 : 0;

        List<AppointmentDTO> dtos =
                appointmentRepository
                        .findByPatient_IdAndStatusOrderByAppointmentTimeAsc(id, status)
                        .stream()
                        .map(AppointmentDTO::new)
                        .toList();

        return ResponseEntity.ok(Map.of("appointments", dtos));
    }

    /* =========================
       FILTER BY DOCTOR
    ========================= */
    public ResponseEntity<Map<String, Object>> filterByDoctor(
            String name, Long id) {

        List<AppointmentDTO> dtos =
                appointmentRepository
                        .filterByDoctorNameAndPatientId(name, id)
                        .stream()
                        .map(AppointmentDTO::new)
                        .toList();

        return ResponseEntity.ok(Map.of("appointments", dtos));
    }

    /* =========================
       FILTER BY DOCTOR + CONDITION
    ========================= */
    public ResponseEntity<Map<String, Object>> filterByDoctorAndCondition(
            String condition, String name, long id) {

        int status = condition.equalsIgnoreCase("past") ? 1 : 0;

        List<AppointmentDTO> dtos =
                appointmentRepository
                        .filterByDoctorNameAndPatientIdAndStatus(name, id, status)
                        .stream()
                        .map(AppointmentDTO::new)
                        .toList();

        return ResponseEntity.ok(Map.of("appointments", dtos));
    }

    /* =========================
       PATIENT DETAILS
    ========================= */
    public ResponseEntity<Map<String, Object>> getPatientDetails(String token) {

        String email = tokenService.extractIdentifier(token);
        Patient patient = patientRepository.findByEmail(email);

        return ResponseEntity.ok(Map.of("patient", patient));
    }
}
