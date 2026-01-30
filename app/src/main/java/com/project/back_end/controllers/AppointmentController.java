import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.Map;



@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private Service service;

    @GetMapping("/{date}/{patientName}/{token}")
    public Map<String, Object> getAppointments(
            @PathVariable String patientName,
            @PathVariable LocalDate date,
            @PathVariable String token) {

        service.validateToken(token, "doctor");
        return appointmentService.getAppointment(patientName, date, token);
    }

    @PostMapping("/{token}")
    public ResponseEntity<Map<String, String>> book(
            @RequestBody Appointment appointment,
            @PathVariable String token) {

        service.validateToken(token, "patient");

        int valid = service.validateAppointment(appointment);
        if (valid != 1)
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Invalid appointment"));

        appointmentService.bookAppointment(appointment);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Appointment booked"));
    }

    @PutMapping("/{token}")
    public ResponseEntity<Map<String, String>> update(
            @RequestBody Appointment appointment,
            @PathVariable String token) {

        service.validateToken(token, "patient");
        return appointmentService.updateAppointment(appointment);
    }

    @DeleteMapping("/{id}/{token}")
    public ResponseEntity<Map<String, String>> cancel(
            @PathVariable long id,
            @PathVariable String token) {

        service.validateToken(token, "patient");
        return appointmentService.cancelAppointment(id, token);
    }
}
