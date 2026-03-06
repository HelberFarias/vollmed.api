package med.voll.api.controller;

import med.voll.api.domain.consulta.AgendaDeConsultas;
import med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import med.voll.api.domain.consulta.DadosDetalhamentoConsulta;
import med.voll.api.domain.medico.Especialidade;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@SpringBootTest //anotação para usar nos controllers
@AutoConfigureMockMvc //injeção do MockMVC
@AutoConfigureJsonTesters //anotacao para os BeanValidation
class ConsultaControllerTest {

    @Autowired
    private MockMvc mvc; //classe propria do Spring que faz testes unitários, só vai fazer o teste no controller
    @Autowired
    private JacksonTester<DadosAgendamentoConsulta> dadosAgendamentoConsultaJson; //o tipo no generics tem que ser o mesmo que recebe no metodo no cotroller e esse é o jso que chega da api
    @Autowired
    private JacksonTester<DadosDetalhamentoConsulta> dadosDetalhamentoConsultaJson; //json que a API devolve
    @MockBean //anotacao para mockar um agente e não usar o banco de dados de verdade
    private AgendaDeConsultas agendaDeConsultas;


    @Test
    @DisplayName ("deveria devolver http400 quando informações estao invalidas analisadas pelo Bean")
    @WithMockUser //anotacao pra pedir ao spring um usuario mockado,
    void agendar_cenario1() throws Exception {

        var response = mvc.perform(
                post("/consultas")
                        .contentType(MediaType.APPLICATION_JSON) //define que o corpo da requisição é JSON
                        .content("{}") // corpo da requisição que será convertido para o DTO
                // .with(csrf()) // só se sua Security estiver exigindo CSRF em POST
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value()); //HttpStatus é uma classe que tem um metodo para testar o erro 400
    }

    @Test
    @DisplayName ("Deveria devolver código 200 quando informações forem válidas")
    @WithMockUser
    void agendar_cenario2() throws Exception {
        var data = LocalDateTime.now().plusHours(1);
        var especialidade = Especialidade.CARDIOLOGIA;
        var dadosDetalhamento = new DadosDetalhamentoConsulta(null, 2l, 5l, data);
        when(agendaDeConsultas.agendar(any())).thenReturn(dadosDetalhamento); // quando a minha agenda mockada for chamada independente do parametro (any()) veja se o retorno é o objeto dadosDetalhamento
        var response = mvc.perform(
                post("/consultas")
                        .contentType(MediaType.APPLICATION_JSON) //define que o corpo da requisição é JSON
                        .content (dadosAgendamentoConsultaJson.write(new DadosAgendamentoConsulta(2l, 5l, data, especialidade)).getJson()) // forma feita para não criar a string json manulamente. Pegao tipo do objeto da consulta marcada e faz essa mágica ai
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        var jsonEsperado = dadosDetalhamentoConsultaJson.write(
                dadosDetalhamento).getJson(); //lembrar de usar o getJson() quando for comparar os objetos dos testes
        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado); //pegue o conteudo da resposta e veja se é igual ao conteudo esperado
    }
}