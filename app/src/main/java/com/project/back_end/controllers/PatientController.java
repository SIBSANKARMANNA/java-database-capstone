import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;





@RestController
@RequestMapping("/patient")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @Autowired
    private Service service;

    @PostMapping
    public ResponseEntity<Map<String, String>> signup(
            @RequestBody Patient patient) {

        if (!service.validatePatient(patient)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message",
                            "Patient with email id or phone no already exist"));
        }

        int result = patientService.createPatient(patient);
        return result == 1
                ? ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "Signup successful"))
                : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Internal server error"));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(
            @RequestBody Login login) {
        return service.validatePatientLogin(login);
    }

    @GetMapping("/{token}")
    public ResponseEntity<Map<String, Object>> getProfile(
            @PathVariable String token) {

        ResponseEntity<Map<String, String>> auth =
                service.validateToken(token, "patient");
        if (!auth.getBody().isEmpty())
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        return patientService.getPatientDetails(token);
    }

    @GetMapping("/{id}/{token}")
    public ResponseEntity<Map<String, Object>> getAppointments(
            @PathVariable Long id,
            @PathVariable String token) {

        ResponseEntity<Map<String, String>> auth =
                service.validateToken(token, "patient");
        if (!auth.getBody().isEmpty())
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        return patientService.getPatientAppointment(id, token);
    }

    @GetMapping("/filter/{condition}/{name}/{token}")
    public ResponseEntity<Map<String, Object>> filterAppointments(
            @PathVariable String condition,
            @PathVariable String name,
            @PathVariable String token) {

        ResponseEntity<Map<String, String>> auth =
                service.validateToken(token, "patient");
        if (!auth.getBody().isEmpty())
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        return service.filterPatient(condition, name, token);
    }
}
