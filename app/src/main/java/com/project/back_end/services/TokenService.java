import java.util.List;
import java.util.Map;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;




@Component
public class TokenService {

    @Value("${jwt.secret}")
    private String secret;

    @Autowired private AdminRepository adminRepo;
    @Autowired private DoctorRepository doctorRepo;
    @Autowired private PatientRepository patientRepo;

    public String generateToken(String identifier) {
        return Jwts.builder()
                .setSubject(identifier)
                .setIssuedAt(new Date())
                .setExpiration(Date.from(
                        Instant.now().plus(7, ChronoUnit.DAYS)))
                .signWith(getSigningKey())
                .compact();
    }

    public String extractIdentifier(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token, String user) {
        try {
            String id = extractIdentifier(token);

            return switch (user) {
                case "admin" -> adminRepo.findByUsername(id) != null;
                case "doctor" -> doctorRepo.findByEmail(id) != null;
                case "patient" -> patientRepo.findByEmail(id) != null;
                default -> false;
            };
        } catch (Exception e) {
            return false;
        }
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
}
