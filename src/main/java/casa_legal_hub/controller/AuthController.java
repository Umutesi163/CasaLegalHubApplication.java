package casa_legal_hub.controller;

import casa_legal_hub.dto.LoginRequestDTO;
import casa_legal_hub.dto.LoginResponseDTO;
import casa_legal_hub.dto.UserRequestDTO;
import casa_legal_hub.dto.UserResponseDTO;
import casa_legal_hub.model.User;
import casa_legal_hub.repository.UserRepository;
import casa_legal_hub.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO dto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
        );
        User user = userRepository.findByUsername(dto.getUsername()).orElseThrow();
        String token = jwtUtil.generateToken(user.getUsername());
        return ResponseEntity.ok(new LoginResponseDTO(token, user.getUsername(), user.getRole().name()));
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserRequestDTO dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(dto.getRole());
        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully");
    }
}
