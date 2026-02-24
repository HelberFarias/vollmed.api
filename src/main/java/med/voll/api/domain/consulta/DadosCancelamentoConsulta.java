package med.voll.api.domain.consulta;

import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;

public record DadosCancelamentoConsulta(
        @NotNull
        Long idConsulta,
        @NotNull @Enumerated
        MotivoCancelamento cancelamento) {}
