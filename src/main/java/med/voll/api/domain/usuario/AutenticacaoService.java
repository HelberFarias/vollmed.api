package med.voll.api.domain.usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
//Classe do servico de autenticar o usuario
@Service
public class AutenticacaoService implements UserDetailsService { //esse implements serve pra avisar ao SpringSecurity que que se trata de uma classe de autenticacao

    @Autowired
    private UsuarioRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByLogin(username); //é preciso entrar no repository para ter um retorno nesse metodo
    }
}
