//package com.guvi.inventory.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//@Configuration
//@EnableWebSecurity
//@EnableMethodSecurity(prePostEnabled = true)
//public class SecurityConfig {
//
//    private final JwtUtil jwtUtil;
//
//    public SecurityConfig(JwtUtil jwtUtil) {
//        this.jwtUtil = jwtUtil;
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public JwtFilter jwtFilter() {
//        return new JwtFilter(jwtUtil);
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//
//        http
//                .csrf(csrf -> csrf.disable())
//
//                .sessionManagement(session ->
//                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                )
//
//                .authorizeHttpRequests(auth -> auth
//                        // ✅ Allow auth APIs
//                        .requestMatchers("/api/auth/**").permitAll()
//
//                        // ✅ Allow Swagger (optional but useful)
//                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
//
//                        // ✅ Role-based access
//                        .requestMatchers("/api/products/**").hasRole("ADMIN")
//                        .requestMatchers("/api/categories/**").hasRole("ADMIN")
//                        .requestMatchers("/api/inventory/**").hasRole("ADMIN")
//                        .requestMatchers("/api/orders/**").hasRole("USER")
//
//                        // ✅ Everything else secured
//                        .anyRequest().authenticated()
//                )
//
//                // ✅ Add JWT filter
//                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }
//}

package com.guvi.inventory.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class SecurityConfig extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public SecurityConfig(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // ✅ ADD THIS BLOCK AT THE VERY TOP
        String path = request.getServletPath();

        if (path.startsWith("/api/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 🔽 Existing JWT logic below
        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);

        // validate token, set authentication...

        filterChain.doFilter(request, response);
    }
}