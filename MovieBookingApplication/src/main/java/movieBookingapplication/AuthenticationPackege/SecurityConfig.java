package movieBookingapplication.AuthenticationPackege;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationfilter jwtAuthenticationfilter;

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder(){
         return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.csrf(csrf->csrf.disable())

                .authorizeHttpRequests(auth -> auth

                        // PUBLIC APIs (Permit All)
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers(
                                // Authentication
                                "/auth/registernormaluser",
                                "/auth/registeradminuser",
                                "/auth/login",
                                "/auth/send-otp",
                                "/auth/forgot-password",
                                "/auth/reset-password",

                                // Movies
                                "/movie/getall",
                                "/movie/genre/**",
                                "/movie/title/**",
                                "/movie/language/**",

                                // Shows
                                "/show/getall",
                                "/show/getshowbymovie/**",
                                "/show/getbytheater/**",

                                // Screens
                                "/screen/getall",
                                "/screen/theater/**",

                                // Seats
                                "/seat/getall",
                                "/seat/get/**",
                                "/seat/screen/**",

                                // Show Seats
                                "/show-seat/show/**",

                                // Theater
                                "/theater/get/**"
                        ).permitAll()

                        // USER APIs
                        .requestMatchers(
                                "/booking/create",
                                "/booking/confirm/**",
                                "/booking/cancel/**",
                                "/booking/userid/**",
                                "/show-seat/lock"
                        ).hasRole("USER")

                        // ADMIN APIs
                        .requestMatchers(
                                // Movie
                                "/movie/create",
                                "/movie/update/**",
                                "/movie/delete/**",

                                // Theater
                                "/theater/create",
                                "/theater/update/**",
                                "/theater/delete/**",

                                // Screen
                                "/screen/create",
                                "/screen/delete/**",

                                // Seat
                                "/seat/create",
                                "/seat/delete/**",

                                // Show
                                "/show/create",
                                "/show/update/**",
                                "/show/delete/**",

                                // Booking Management
                                "/booking/getall",
                                "/booking/showId/**",
                                "/booking/getbystatus/**"

                        ).hasRole("ADMIN")
                        .anyRequest().authenticated()//Any Other Request
                )

                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(
                        jwtAuthenticationfilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider =
                new DaoAuthenticationProvider(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public UserDetailsService userDetailsService(){
        return new CustomUserDetailsService();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration
                                                               configuration)throws Exception{
        return configuration.getAuthenticationManager();
    }

}
