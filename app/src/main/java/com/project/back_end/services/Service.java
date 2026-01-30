import java.util.List;
import java.util.Map;
import java.time.LocalDate;
import java.time.LocalDateTime;




@Service
public class Service {

    private final TokenService tokenService;
    private final AdminRepository adminRepo;
    private final DoctorRepository doctorRepo;
    private final PatientRepository patientRepo;
    private final DoctorService doctorService;
    private final PatientService patientService;

    public Service(
            TokenService tokenService,
            AdminRepository adminRepo,
            DoctorRepository doctorRepo,
            PatientRepository patientRepo,
            DoctorService doctorService,
            PatientService patientService
    ) {
        this.tokenService = tokenService;
        this.adminRepo = adminRepo;
        this.doctorRepo = doctorRepo;
        this.patientRepo = patientRepo;
        this.doctorService = doctorService;
        this.patientService = patientService;
    }

    public ResponseEntity<Map<String, String>> validateToken(String token, String user) {
        if (!tokenService.validateToken(token, user)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid or expired token"));
        }
        return ResponseEntity.ok(Map.of());
    }

    public ResponseEntity<Map<String, String>> validateAdmin(Admin admin) {
        Admin dbAdmin = adminRepo.findByUsername(admin.getUsername());
        if (dbAdmin != null && dbAdmin.getPassword().equals(admin.getPassword())) {
            String token = tokenService.generateToken(admin.getUsername());
            return ResponseEntity.ok(Map.of("token", token));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "Invalid credentials"));
    }

    public boolean validatePatient(Patient patient) {
        return patientRepo.findByEmailOrPhone(
                patient.getEmail(), patient.getPhone()) == null;
    }

    public ResponseEntity<Map<String, String>> validatePatientLogin(Login login) {
        Patient patient = patientRepo.findByEmail(login.getIdentifier());
        if (patient != null && patient.getPassword().equals(login.getPassword())) {
            String token = tokenService.generateToken(patient.getEmail());
            return ResponseEntity.ok(Map.of("token", token));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "Invalid credentials"));
    }

    public int validateAppointment(Appointment appointment) {
        Optional<Doctor> doctorOpt = doctorRepo.findById(appointment.getDoctor().getId());
        if (doctorOpt.isEmpty()) return -1;

        List<String> slots =
                doctorService.getDoctorAvailability(
                        appointment.getDoctor().getId(),
                        appointment.getAppointmentTime().toLocalDate());

        return slots.contains(appointment.getAppointmentTime().toLocalTime().toString()) ? 1 : 0;
    }

    public Map<String, Object> filterDoctor(String name, String specialty, String time) {
        return doctorService.filterDoctorsByNameSpecilityandTime(name, specialty, time);
    }

    public ResponseEntity<Map<String, Object>> filterPatient(
            String condition, String name, String token) {

        String email = tokenService.extractIdentifier(token);
        Patient patient = patientRepo.findByEmail(email);

        if (patient == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (!"null".equals(condition) && !"null".equals(name)) {
            return patientService.filterByDoctorAndCondition(condition, name, patient.getId());
        } else if (!"null".equals(condition)) {
            return patientService.filterByCondition(condition, patient.getId());
        } else {
            return patientService.filterByDoctor(name, patient.getId());
        }
    }
}
