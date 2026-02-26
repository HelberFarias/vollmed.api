package med.voll.api.domain.medico;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest //anotação para usar em testes repository
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) //anotacao para o teste usar o nosso banco de dados e nao o da memoria

class MedicoRepositoryTest {

    @Test
    void escolherMedicoAleatorioLivreNaData() {
    }
}