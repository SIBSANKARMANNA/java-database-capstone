import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;




@RestController
@RequestMapping("${api.path}prescription")
public class PrescriptionController {

    @Autowired
    private PrescriptionService prescriptionService;

    @Autowired
    private Service service;

    @PostMapping("/{token}")
    public ResponseEntity<Map<String, String>> save(
            @RequestBody Prescription prescription,
            @PathVariable String token) {

        service.validateToken(token, "doctor");
        return prescriptionService.savePrescription(prescription);
    }

    @GetMapping("/{appointmentId}/{token}")
    public ResponseEntity<Map<String, Object>> get(
            @PathVariable Long appointmentId,
            @PathVariable String token) {

        service.validateToken(token, "doctor");
        return prescriptionService.getPrescription(appointmentId);
    }
}
