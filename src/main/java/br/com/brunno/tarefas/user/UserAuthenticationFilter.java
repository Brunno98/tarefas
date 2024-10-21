package br.com.brunno.tarefas.user;

import br.com.brunno.tarefas.config.SecurityConfiguration;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class UserAuthenticationFilter extends OncePerRequestFilter {

    private final JWTTokenService jwtTokenService;

    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (isPublicEndpoint(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = recoveryToken(request);
        if (token == null) {
            throw new RuntimeException("O token esta ausente");
        }

        String subject = jwtTokenService.getSubjectFromToken(token);
        User user = userRepository.findByEmail(subject)
                .orElseThrow(() -> new RuntimeException("Usuario nao encontrado"));
        UserDetailsImpl userDetails = new UserDetailsImpl(user);

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

        filterChain.doFilter(request, response);
    }

    private String recoveryToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (authorization != null) {
            return authorization.replace("Bearer ", "");
        }
        return null;
    }

    private boolean isPublicEndpoint(String enpoint) {
        return Arrays.asList(SecurityConfiguration.ENPOINTS_WITH_AUTHENTICATION_NOT_REQUIRED).contains(enpoint);
    }
}
