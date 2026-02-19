package med.voll.api.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import med.voll.api.domain.usuario.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;


// CLASSE PARA GERAR O TOKEN
@Service
public class TokenService {
    @Value("${api.security.token.secret}") //essa chave da lá no application.properties
    private String secret;

    public String gerarToken(Usuario usuario) {
        try {
            var algoritmo = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("API Voll.med") //quem estar gerando o token?
                    .withSubject(usuario.getLogin()) //passar a informação do login no token
                    .withClaim("id", usuario.getId()) //passar a informação do id no token
                    .withExpiresAt(dataExpiracao()) //IMPORTANTE TER UM LIMITE DE TEMPO DE USO
                    .sign(algoritmo);
        } catch (JWTCreationException e){
            throw new RuntimeException("erro ao gerar o token", e);
        }
    }

    public String getSubject (String tokenJWT) {
        try {
            var algoritmo = Algorithm.HMAC256(secret);
            return JWT.require(algoritmo)
                    .withIssuer("API Voll.med")
                    .build()
                    .verify(tokenJWT)
                    .getSubject();

        } catch (JWTVerificationException exception) {
            throw new RuntimeException("TokenJWT invalido ou expirado!");
        }
    }

    private Instant dataExpiracao() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00")); //no Brasil o horário é -3:00h
    }
}
