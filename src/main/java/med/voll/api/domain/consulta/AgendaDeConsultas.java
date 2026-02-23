package med.voll.api.domain.consulta;

import med.voll.api.domain.ValidacaoException;
import med.voll.api.domain.consulta.validacoes.ValidadorAgendamentoDeConsulta;
import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.medico.MedicoRepository;
import med.voll.api.domain.paciente.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// as classes services executa as regras de negocios e validacoes da aplicacao
@Service
public class AgendaDeConsultas {

    @Autowired
    private ConsultaRepository consultaRepository;
    @Autowired
    private MedicoRepository medicoRepository;
    @Autowired
    private PacienteRepository pacienteRepository;
    @Autowired
    private List<ValidadorAgendamentoDeConsulta> validadores; //injetar uma lista de validadores

    public DadosDetalhamentoConsulta agendar (DadosAgendamentoConsulta dados) { //esse metodo terá todas as validacoes que ta no trello

        if (!pacienteRepository.existsById(dados.idPaciente())) { //validacao de integridade
            throw new ValidacaoException("Id do paciente informado é inexistente!");
        }
        if (dados.idMedico() != null && !medicoRepository.existsById(dados.idMedico())) { //validamos dessa forma pq talvez o id do medico não venha, já que a escolhe dele é opcional (regra no trello)
            throw new ValidacaoException("Id do médico informado é inexistente!");
        }

        validadores.forEach(v -> v.validar(dados));

        var medico = escolherMedico(dados);
        var paciente = pacienteRepository.getReferenceById(dados.idPaciente());
        var consulta = new Consulta(null, medico, paciente, dados.data()); //primeiro atributo é null pq o proprio banco de dados que vai gerar o id
        consultaRepository.save(consulta);
        return new DadosDetalhamentoConsulta(consulta);

    }

    private Medico escolherMedico(DadosAgendamentoConsulta dados) { //temos que devolver um objeto do tipo Medico
       if (dados.idMedico() != null) {
           return medicoRepository.getReferenceById(dados.idMedico());
       }
       if (dados.especialidade() == null) {
           throw new ValidacaoException("Campo ESPECIALIDADE é obrigatorio!");
       }
        return medicoRepository.escolherMedicoAleatorioLivreNaData(dados.especialidade(), dados.data());
    }

}
