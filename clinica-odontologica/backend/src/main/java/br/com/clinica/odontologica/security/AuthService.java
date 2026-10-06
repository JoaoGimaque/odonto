package br.com.clinica.odontologica.security;

import br.com.clinica.odontologica.domain.AppUser;
import br.com.clinica.odontologica.dto.AuthResponse;
import br.com.clinica.odontologica.dto.LoginRequest;
import br.com.clinica.odontologica.repository.AppUserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final AppUserRepository users;
    private final PasswordEncoder encoder;
    private final JwtService jwt;
    public AuthService(AppUserRepository users, PasswordEncoder encoder, JwtService jwt) { this.users=users; this.encoder=encoder; this.jwt=jwt; }
    public AuthResponse login(LoginRequest request) {
        AppUser user = users.findByEmailIgnoreCase(request.email().trim()).filter(AppUser::isEnabled)
                .filter(u -> encoder.matches(request.password(), u.getPasswordHash())).orElseThrow(() -> new BadCredentialsException("E-mail ou senha inválidos."));
        return new AuthResponse(jwt.generate(user), "Bearer", user.getId(), user.getName(), user.getEmail(), user.getRole().name());
    }
}
