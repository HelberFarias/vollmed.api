package med.voll.api.infra.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import med.voll.api.domain.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;
    @Autowired
    UsuarioRepository repository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        var tokenJWT = recuperarToken(request); //metodo que recupera o token do cabeçalho
        if (tokenJWT != null) { //se tem o token no cabeçalho
            var subject = tokenService.getSubject(tokenJWT); //valida o token mas ainda e preciso avisar ao spring que a pessoa ta logada
            var usuario = repository.findByLogin(subject); //passa o subject pq aqui que ta o login do usuario

            var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities()); //eh preciso instanciar esse objeto enorme ai,tipo um DTO
            SecurityContextHolder.getContext().setAuthentication(authentication); //classe do spring que autentica o usuario na requisicao de forma forcada (set)
            System.out.println("Logado!");
        }
        filterChain.doFilter(request, response);
    }

    private String recuperarToken(HttpServletRequest request) { //logica para recuperar o token
        var authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null) {
            return authorizationHeader.replace("Bearer ", "").replace("\"", "").trim();
        }
        return null;
    }
}
