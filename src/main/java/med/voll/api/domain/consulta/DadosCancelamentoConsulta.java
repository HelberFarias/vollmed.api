package med.voll.api.domain.consulta;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;

public record DadosCancelamentoConsulta(
        @NotNull
        Long idConsulta,
        @NotNull @Enumerated (EnumType.STRING)
        MotivoCancelamento motivo) {}
