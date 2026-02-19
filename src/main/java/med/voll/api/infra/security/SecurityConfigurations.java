package med.voll.api.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
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

//classe que vamos concentrar as configurações de segurança


@Configuration
@EnableWebSecurity //personalizar as configurações de segurança
public class SecurityConfigurations {

   @Autowired
   private SecurityFilter securityFilter;


    //o Bean serve para exportar uma classe para o Spring, fazendo com que ele consiga carrega-la e realize a sua injeção
    //de dependencia em outras classes
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return
                http.csrf(csrf -> csrf.disable())
                        .sessionManagement(sm ->
                                sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                        .authorizeHttpRequests(auth -> { //metodo que configura como que sera as autorizacoes das requisicoes
                            auth.requestMatchers("/login").permitAll(); //o login é a unica requisicao que deve ser aberta
                            auth.anyRequest().authenticated(); //qualquer outra requisição, só pode se estiver autenticado
                        })
                        .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class) //primeira chama o nosso filtro que é a classe toda SecurityFilter, depois o filtro do Spring
                        .build();
    }

    // metodo para autenticacao
    @Bean
    public AuthenticationManager manager (AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager(); //metodo requer uma exception
    }

    // metodo para avisar ao Spring que iremos usar o BCrypt como hash na senha
    @Bean
    public PasswordEncoder passwordEncoder () {
        return new BCryptPasswordEncoder();
    }
}
