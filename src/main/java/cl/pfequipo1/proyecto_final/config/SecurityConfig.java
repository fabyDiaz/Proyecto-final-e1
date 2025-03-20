package cl.pfequipo1.proyecto_final.config;

import cl.pfequipo1.proyecto_final.service.AdminUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private AdminUserDetailsService adminUserDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

       return httpSecurity
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> {
                    // Ruta de acceso a la documentacion
                    auth.requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll();
                    // Rutas públicas para visualizar compañías y localidades
                    auth.requestMatchers(HttpMethod.GET, "/api/v1/companies/**").permitAll();
                    auth.requestMatchers(HttpMethod.GET, "/api/v1/locations/**").permitAll();
                    auth.requestMatchers(HttpMethod.GET, "/api/v1/sensor/**").permitAll();
                    auth.requestMatchers(HttpMethod.GET, "/api/v1/sensor_data/**").permitAll();

                    // Rutas NUEVAS específicas para admin que incluyen apiKey
                    auth.requestMatchers(HttpMethod.GET, "/api/v1/admin/companies/**").hasRole("ADMIN");

                    // Rutas protegidas para modificaciones (POST, PUT, DELETE)
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/companies/**").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.PUT, "/api/v1/companies/**").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.DELETE, "/api/v1/companies/**").hasRole("ADMIN");

                    auth.requestMatchers(HttpMethod.POST, "/api/v1/locations/**").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.PUT, "/api/v1/locations/**").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.DELETE, "/api/v1/locations/**").hasRole("ADMIN");

                    // Cualquier otra ruta debe estar autenticada
                    auth.anyRequest().authenticated();

                })
                .httpBasic(Customizer.withDefaults())
                .build();

    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(adminUserDetailsService);
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

   /* @Bean
    public UserDetailsService userDetailsService(){
        return (UserDetailsService) User.withUsername("Fabiola").password("123").roles("ADMIN").authorities("READ","CREATE").build();
    }
    */


}
