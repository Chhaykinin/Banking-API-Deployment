package springgradle.bankingproject.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    //  @Bean
//    InMemoryUserDetailsManager configuredUserSecurity(){
//        UserDetails user = User.
//                withUsername("admin").
//                password(passwordEncoder.encode("admin123")).
//                roles("USER","ADMIN").build();
//        UserDetails user1 = User.
//                withUsername("user1").
//                password(passwordEncoder.encode("user1123")).
//                roles("USER","USER1").build();
//        UserDetails user2 = User.
//                withUsername("user2").
//                password(passwordEncoder.encode("user2123")).
//                roles("USER","USER2").build();
//        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//        manager.createUser(user);
//        manager.createUser(user1);
//        manager.createUser(user2);
//        return manager;
//    }
    @Bean
    JwtAuthenticationProvider configJwtAuthenticationProvider(@Qualifier("refreshTokenJwtDecoder") JwtDecoder refreshTokenDecoder) {
        return new JwtAuthenticationProvider(refreshTokenDecoder);
    }

    @Bean
    DaoAuthenticationProvider configDaoAuthenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userDetailsService);
        auth.setPasswordEncoder(passwordEncoder);
        return auth;
    }
    @Bean
    SecurityFilterChain configureApiSecurity(HttpSecurity http, @Qualifier("accessTokenJwtDecoder")JwtDecoder jwtDecoder) throws Exception {
        // Endpoint security config
        http.authorizeHttpRequests(endpoint -> endpoint.
                requestMatchers("/api/v1/auth/**","/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html","api/v1/upload/**").permitAll().
                requestMatchers(HttpMethod.POST,"/api/v1/accounts-type/**").hasAuthority("SCOPE_ROLE_USER").
                requestMatchers(HttpMethod.GET,"/api/v1/accounts-types/**").hasAnyRole("MANAGER","ADMIN").
                requestMatchers(HttpMethod.PUT,"/api/v1/accounts-types/**").hasAnyRole("MANAGER","ADMIN").
                requestMatchers(HttpMethod.PATCH,"/api/v1/accounts-types/**").hasAnyRole("MANAGER","ADMIN").
//                requestMatchers(HttpMethod.DELETE,"/api/v1/accounts").hasAnyRole("USER","ADMIN").
                anyRequest().authenticated());
        //Security mechanism (http basic auth
        //Http basic auth (username & pass)
       // http.httpBasic(Customizer.withDefaults());

        // new security mechanism jwt , not use default
        http.oauth2ResourceServer(jwt -> jwt.
                jwt(jwtConfigurer ->jwtConfigurer
                        .decoder(jwtDecoder)));

        // Disable CSRF token
        http.csrf(AbstractHttpConfigurer::disable);
        // make stateless session : do have security
        http.sessionManagement(session -> session.
                sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }
}
