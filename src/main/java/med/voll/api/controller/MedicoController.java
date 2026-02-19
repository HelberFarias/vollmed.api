package med.voll.api.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.domain.medico.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
// EVITAR USAR ENTIDADES DIRETAMENTE NOS CONTROLLES COMO SERIA LIST<MEDICO> ABAIXO. VEM TODOS OS DADOS.

@RestController
@RequestMapping("/medicos")
public class MedicoController {
    @Autowired
    private MedicoRepository repository;

    @PostMapping
    @Transactional
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroMedico dados, UriComponentsBuilder uriBuilder) { //usa o Valid aqui pra o Bean validar tudo em cascata
        var medico = new Medico(dados);
        repository.save(medico);
        var uri = uriBuilder.path("/medicos/{}").buildAndExpand(medico.getId()).toUri();
        //buildAndExpand espera um id do medico criado
        //por isso criamos a variacao para guardar o objeto medico + o id do medico que é esperado pelo metodo
        return ResponseEntity.created(uri).body(new DadosDetalhamentoMedico(medico));
        //created espera uma uri, por isso criamos ela acima
        //e o body espera um dto com os dados que iremos cadastrar para representar o body
        // OBSERVÇAO IMPORTANTE: esse novo DTO tem que ser completo porque ta criando um novo medico completo
    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemMedicos>> listar(@PageableDefault(size = 10, sort = {"nome"}) Pageable pageable) { //sempre que trabalhar com paginação, usar o tipo Page e interface Pageable e a anotação é a configuraçãod a paginacao
        // return repository.findAllByAtivoTrue(pageable).map(DadosListagemMedicos::new); // metodo antigo antes de usar ResponseEntity
        var page = repository.findAllByAtivoTrue(pageable).map(DadosListagemMedicos::new);
        return ResponseEntity.ok(page);
    }

    @PutMapping
    @Transactional
    public ResponseEntity atualizar(@RequestBody @Valid DadosAtualizacaoMedica dados) {
        var medico = repository.getReferenceById(dados.id());
        medico.atualizarInformacoes(dados);
        return ResponseEntity.ok(new DadosDetalhamentoMedico(medico));
        //em metodo de atualizar, é interessante devolver o objeto/informação que foi atualizada para isso criamos um novo DTO
        //não vamos usar o DadosAtualizaçãoMedica pq ele é incompleta, ele representa apenas os dados da atualização do medico
        //e aqui nós vamos devolver todas as infomações do médico e assim, criamos um novo DTO (DadosDetalhamentoMedico)
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity excluir(@PathVariable Long id){
        var medico = repository.getReferenceById(id);
        medico.excluir();
        return ResponseEntity.noContent().build(); //apenas o metodo nContent não devolve um ResponseEntity, por isso usa o build para construi-lo
    }
    @GetMapping ("/{id}")
    public ResponseEntity detalhar(@PathVariable Long id) {
        var medico = repository.getReferenceById(id); //carregar o medico do campo de dados pelo ID
        return ResponseEntity.ok(new DadosDetalhamentoMedico(medico)); //criar um novo DTO do medico carregado do repositorio
    }


}
