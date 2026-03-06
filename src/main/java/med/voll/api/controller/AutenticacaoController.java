package med.voll.api.controller;

import jakarta.validation.Valid;
import med.voll.api.domain.usuario.DadosAutenticaco;
import med.voll.api.domain.usuario.Usuario;
import med.voll.api.infra.security.DadosTokenJWT;
import med.voll.api.infra.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AutenticacaoController {
    @Autowired
    private AuthenticationManager manager; //classe que usamos para instanciar a classe de autenticacao (nao pode instanciar a classe de autenticacao de forma direta)
    @Autowired
    private TokenService tokenService ;

    @PostMapping
    public ResponseEntity efetuarLogin(@RequestBody @Valid DadosAutenticaco dados) { //dto criado para receber login e senha
        var authenticationToken = new UsernamePasswordAuthenticationToken ( //instanciamos esse objeto para converter um tipo de DTO para outro que o authentication exige
                dados.login(),
                dados.senha());
        var authentication = manager.authenticate(authenticationToken);
        var tokenJWT = tokenService.gerarToken((Usuario) authentication.getPrincipal()); //casting feito
        return ResponseEntity.ok(new DadosTokenJWT(tokenJWT));
    }
}
