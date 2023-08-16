package com.backend.ecommerce.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebSecurity
@EnableWebMvc
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**").permitAll()    //accessible to all
//
//                        USER APIs
                                .requestMatchers(HttpMethod.GET,"/api/users").hasAnyAuthority("ADMIN","USER")
                                .requestMatchers(HttpMethod.GET,"/api/users/{id}").hasAnyAuthority("USER","MERCHANT","ADMIN")
                                .requestMatchers(HttpMethod.PUT,"/api/users").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.DELETE,"/api/users/**").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.GET,"/api/users/{id}/products").hasAnyAuthority("MERCHANT","ADMIN","USER")
                                .requestMatchers(HttpMethod.GET,"/api/users/{id}/orders").hasAnyAuthority("USER","MERCHANT","ADMIN")
                                .requestMatchers(HttpMethod.GET,"/api/users/merchants").hasAnyAuthority("USER","MERCHANTS","ADMIN")
//                        Product APIs
                                .requestMatchers(HttpMethod.GET,"/api/products").hasAnyAuthority("USER","MERCHANT","ADMIN")
                                .requestMatchers(HttpMethod.GET,"/api/products/**").hasAnyAuthority("USER","MERCHANT","ADMIN")
                                .requestMatchers(HttpMethod.POST,"api/products").hasAnyAuthority("MERCHANT","ADMIN")
                                .requestMatchers(HttpMethod.PUT,"api/products").hasAnyAuthority("MERCHANT","ADMIN")
                                .requestMatchers(HttpMethod.DELETE,"api/products").hasAnyAuthority("ADMIN")
                                .requestMatchers(HttpMethod.GET,"api/products/{id}/like").hasAnyAuthority("USER","ADMIN")
                                .requestMatchers(HttpMethod.GET,"api/products/filter/**").hasAnyAuthority("USER","ADMIN","MERCHANT")
                                .requestMatchers(HttpMethod.GET,"api/products/query**").hasAnyAuthority("USER","ADMIN","MERCHANT")
                                .requestMatchers(HttpMethod.GET,"api/products/search**").hasAnyAuthority("USER","ADMIN","MERCHANT")
//                        Cart APIs
                                .requestMatchers(HttpMethod.POST,"api/carts").hasAnyAuthority("USER","ADMIN")
                                .requestMatchers(HttpMethod.DELETE,"api/carts/**").hasAnyAuthority("USER","ADMIN")
                                .requestMatchers(HttpMethod.GET,"api/carts/**").hasAnyAuthority("USER","ADMIN")
//                        Order APIs
                                .requestMatchers(HttpMethod.GET,"api/orders").hasAnyAuthority("ADMIN","USER")
                                .requestMatchers(HttpMethod.POST,"api/orders").hasAnyAuthority("USER","ADMIN")
                                .requestMatchers(HttpMethod.GET,"api/orders/{id}").hasAnyAuthority("USER","MERCHANT","ADMIN")
//                        Category APIs
                                .requestMatchers(HttpMethod.GET,"/api/category").hasAnyAuthority("USER","MERCHANT","ADMIN")
                                .requestMatchers(HttpMethod.POST,"/api/category").hasAnyAuthority("ADMIN")
                                .requestMatchers(HttpMethod.GET,"/api/category/{name}").hasAnyAuthority("USER","MERCHANT","ADMIN")
                                .requestMatchers(HttpMethod.GET,"/api/category/{id}").hasAnyAuthority("USER","MERCHANT","ADMIN")
                                .requestMatchers(HttpMethod.GET,"/api/category/{name}/products").hasAnyAuthority("USER","MERCHANT","ADMIN")

                                .requestMatchers(HttpMethod.GET,"/v3/api-docs").permitAll()
                                .requestMatchers(HttpMethod.GET,"v3/api-docs/**").permitAll()
                                .requestMatchers(HttpMethod.GET,"/swagger-ui.html").permitAll()
                                .requestMatchers(HttpMethod.GET,"/swagger-ui/**").permitAll()
                .requestMatchers("/api/**").hasAuthority("ADMIN")
                .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter,UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        return new CorsConfigurationSource() {
            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                CorsConfiguration corsConfiguration=new CorsConfiguration();
                corsConfiguration.addAllowedOrigin("*");
                corsConfiguration.addAllowedHeader("*");
                corsConfiguration.addAllowedOriginPattern("**");
                corsConfiguration.addAllowedMethod(HttpMethod.GET);
                corsConfiguration.addAllowedMethod(HttpMethod.POST);
                corsConfiguration.addAllowedMethod(HttpMethod.PUT);
                corsConfiguration.addAllowedMethod(HttpMethod.DELETE);
                return corsConfiguration;
            }
        };
    }
}
