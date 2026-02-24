package med.voll.api.domain.consulta;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import med.voll.api.domain.ValidacaoException;
import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.paciente.Paciente;
import java.time.LocalDateTime;

import static med.voll.api.domain.consulta.StatusConsulta.AGENDADA;


@Table (name = "consultas")
@Entity (name = "Consultas")
@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = "id")

public class Consulta {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "medico_id")
    private Medico medico;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "paciente_id")
    private Paciente paciente;
    private LocalDateTime data;
    @Enumerated (EnumType.STRING)
    private MotivoCancelamento motivoCancelamento;
    @Enumerated (EnumType.STRING)
    private StatusConsulta status = StatusConsulta.AGENDADA;
    private LocalDateTime dataCancelamento;

    public Consulta(Long id, Medico medico, Paciente paciente, LocalDateTime data) {
        this.id = id;
        this.medico = medico;
        this.paciente = paciente;
        this.data = data;
        this.status = StatusConsulta.AGENDADA;
    }


    public void cancelarConsulta(MotivoCancelamento motivo) {
        if (motivo == null) {
            throw new ValidacaoException("[ERRO] Campo Motivo obrigatorio!");
        }
        if (status == StatusConsulta.REALIZADA) {
            throw new ValidacaoException("[ERRO] Consulta já realizada!");
        }
        if (status == StatusConsulta.CANCELADA) {
            throw new ValidacaoException("[ERRO] Consulta já cancelada!");
        }
        this.status = StatusConsulta.CANCELADA;
    }
}
