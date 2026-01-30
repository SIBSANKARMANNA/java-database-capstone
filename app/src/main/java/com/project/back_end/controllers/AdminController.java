import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.Map;




@RestController
@RequestMapping("${api.path}admin")
public class AdminController {

    @Autowired
    private Service service;

    @PostMapping
    public ResponseEntity<Map<String, String>> adminLogin(
            @RequestBody Admin admin) {
        return service.validateAdmin(admin);
    }
}
