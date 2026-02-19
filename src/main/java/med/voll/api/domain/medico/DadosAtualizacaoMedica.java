package med.voll.api.domain.medico;

import jakarta.validation.constraints.NotNull;
import med.voll.api.domain.endereco.DadosEndereco;


public record DadosAtualizacaoMedica(
        @NotNull
        Long id,
        String nome,
        String telefone,
        DadosEndereco endereco)//vamos usar o DTO DadosEndereco pq as validacoes que estao la, tbm servem aqui
 {}
