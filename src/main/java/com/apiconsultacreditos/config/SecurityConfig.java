package seu.pacote.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/***
 * Configuração de segurança basica que você pode
 * parametrizar e controlar a senha no application-docker.properties
 * 
 * **/


@Configuration
public class SecurityConfig {

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
      .authorizeHttpRequests(authz -> authz
        .requestMatchers("/actuator/health", "/actuator/health/**", "/actuator/info").permitAll()
        // .requestMatchers("/api/creditos/**").permitAll() // Libera os endpoints da API
        .anyRequest().authenticated()
      )
      .httpBasic()
      .and()
      .csrf().disable();

    return http.build();
  }
}
