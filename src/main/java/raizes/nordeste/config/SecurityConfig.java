package raizes.nordeste.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import raizes.nordeste.security.JwtAuthenticationFilter;
import raizes.nordeste.security.exception.RestAccessDeniedHandler;
import raizes.nordeste.security.exception.RestAuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(JwtProperties.class)
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final RestAuthenticationEntryPoint authenticationEntryPoint;
    private final RestAccessDeniedHandler accessDeniedHandler;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            RestAuthenticationEntryPoint authenticationEntryPoint,
            RestAccessDeniedHandler accessDeniedHandler) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/produtos/**", "/unidades/**", "/estoques/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/produtos/**").hasRole("FUNCIONARIO")
                        .requestMatchers(HttpMethod.PUT, "/estoques/**").hasRole("FUNCIONARIO")
                        .requestMatchers(HttpMethod.PATCH, "/unidades/**").hasRole("FUNCIONARIO")
                        //.requestMatchers(HttpMethod.POST, "/usuarios/**").hasRole("FUNCIONARIO")
                        .requestMatchers(HttpMethod.POST, "/usuarios/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/usuarios/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/pedidos/unidade/**").hasRole("FUNCIONARIO")
                        .requestMatchers(HttpMethod.POST, "/pedidos/**").hasAnyRole("CLIENTE", "TOTEN")
                        .requestMatchers(HttpMethod.PATCH, "/pedidos/*/status").hasRole("FUNCIONARIO")
                        .requestMatchers(HttpMethod.PATCH, "/pedidos/*/cancelar").hasAnyRole("CLIENTE", "TOTEN", "FUNCIONARIO")
                        .requestMatchers(HttpMethod.GET, "/pedidos/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/pagamentos/**").hasAnyRole("CLIENTE", "TOTEN")
                        .requestMatchers(HttpMethod.PATCH, "/pagamentos/*/processar").hasRole("FUNCIONARIO")
                        .requestMatchers(HttpMethod.PATCH, "/pagamentos/*/cancelar").hasAnyRole("CLIENTE", "TOTEN", "FUNCIONARIO")
                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
