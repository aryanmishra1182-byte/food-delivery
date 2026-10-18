
package food_delivery.config;

import food_delivery.security.JwtAuthenticationFilter;
import food_delivery.service.CustomUserDetailsService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(
            CustomUserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(userDetailsService);

        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtAuthenticationFilter jwtAuthenticationFilter,
            AuthenticationProvider authenticationProvider) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .authenticationProvider(authenticationProvider)

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .authorizeHttpRequests(auth -> auth

                        // Public registration and login
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/users",
                                "/api/auth/login"
                        ).permitAll()

                        // Public restaurant discovery
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/restaurants",
                                "/api/restaurants/city",
                                "/api/restaurants/search",
                                "/api/restaurants/paginated",
                                "/api/restaurants/*"
                        ).permitAll()

                        // Public menu-category browsing
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/restaurants/*/categories"
                        ).permitAll()

                        // Public menu-item browsing
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/categories/*/items"
                        ).permitAll()

                        // Restaurant owner: view all menu items
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/categories/*/items/manage"
                        ).hasAuthority("RESTAURANT_OWNER")

                        // Restaurant owner: create menu items
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/categories/*/items"
                        ).hasAuthority("RESTAURANT_OWNER")

                        // Restaurant owner: update menu items
                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/categories/*/items/*"
                        ).hasAuthority("RESTAURANT_OWNER")

                        // Restaurant owner: delete menu items
                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/categories/*/items/*"
                        ).hasAuthority("RESTAURANT_OWNER")

                        // Admin operations
                        .requestMatchers("/api/admin/**")
                        .hasAuthority("ADMIN")

                        // Restaurant owner operations
                        .requestMatchers("/api/restaurants/**")
                        .hasAuthority("RESTAURANT_OWNER")

                        // Delivery partner operations
                        .requestMatchers("/api/delivery/**")
                        .hasAuthority("DELIVERY_PARTNER")

                        // Customer operations
                        .requestMatchers("/api/customer/**")
                        .hasAuthority("CUSTOMER")

                        // Deny all unconfigured routes
                        .anyRequest().denyAll()
                )

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration) throws Exception {

        return configuration.getAuthenticationManager();
    }
}
