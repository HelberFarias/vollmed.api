package med.voll.api.domain.medico;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import med.voll.api.domain.endereco.DadosEndereco;


public record DadosCadastroMedico(
        @NotBlank //o atributo não pode ser nulo e nem vazio
        String nome,
        @NotBlank @Email
        String email,
        @NotBlank
        String telefone,
        @NotBlank @Pattern (regexp = "\\d{4,6}") // expressão regular de 4 a 6 digitos
        String crm,
        @NotNull //usado apenas para string, diferente de NotBlank que verifica se o campo ta null ou vazio
        Especialidade especialidade,
        @NotNull @Valid // quando dentro de um DTO tem outro DTO, vc avisa com o Valid que é pra validar o outro DTO
        DadosEndereco endereco) {
}
