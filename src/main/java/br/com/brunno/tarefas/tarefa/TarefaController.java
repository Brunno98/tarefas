package br.com.brunno.tarefas.tarefa;

import br.com.brunno.tarefas.user.User;
import br.com.brunno.tarefas.user.UserRepository;
import br.com.brunno.tarefas.user.UsuarioExiste;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/tarefas")
public class TarefaController {

    private final UserRepository userRepository;
    private final TarefaRepository tarefaRepository;

    @PostMapping
    public TarefaCriadaDto criaNovaTarefa(
            @RequestBody @Valid NovaTarefaDto novaTarefaDto,
            @AuthenticationPrincipal(errorOnInvalidType = true) @UsuarioExiste String username
    ) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("Usuario " + username + " nao encontrado."));
        Tarefa tarefa = novaTarefaDto.novaTarefa(user);
        tarefaRepository.save(tarefa);
        return new TarefaCriadaDto(tarefa);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void excluiTarefa(
            @PathVariable Long id,
            @AuthenticationPrincipal(errorOnInvalidType = true) @UsuarioExiste String username
    ) {
        Tarefa tarefa = tarefaRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Id " + id + " de tarefa não encontrado")
        );
        if (!tarefa.temResponsavel(username)) {
            throw new IllegalArgumentException("Usuario "+username+" não é responsavel pela tarefa "+id);
        }
        tarefaRepository.delete(tarefa);
    }

    @GetMapping()
    public List<TarefaDto> listaTarefas(
            @AuthenticationPrincipal(errorOnInvalidType = true) @UsuarioExiste String username
    ) {
        List<Tarefa> tarefas = this.tarefaRepository.findByResponsavel_Email(username);
        return tarefas.stream().map(TarefaDto::new).collect(Collectors.toList());
    }

    @PostMapping("{id}/estado/{estado}")
    public TarefaDto alteraEstado(
            @PathVariable Long id,
            @PathVariable Estado estado,
            @AuthenticationPrincipal(errorOnInvalidType = true) @UsuarioExiste String username
    ) {
        Tarefa tarefa = this.tarefaRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Tarefa de id " + id + " nao encontrada")
        );
        if (!tarefa.temResponsavel(username)) {
            throw new IllegalArgumentException("Usuario "+username+" não é responsavel pela tarefa "+id);
        }

        tarefa.alteraEstado(estado);
        tarefaRepository.save(tarefa);
        return new TarefaDto(tarefa);
    }

    @PutMapping("{id}")
    public TarefaDto alteraTarefa(
            @PathVariable Long id,
            @RequestBody AlteraTarefaDto alteraTarefaDto,
            @AuthenticationPrincipal(errorOnInvalidType = true) @UsuarioExiste String username
    ) {
        Tarefa tarefa = this.tarefaRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Tarefa de id " + id + " nao encontrada")
        );
        if (!tarefa.temResponsavel(username)) {
            throw new IllegalArgumentException("Usuario "+username+" não é responsavel pela tarefa "+id);
        }

        tarefa.atualizaTarefa(alteraTarefaDto);
        this.tarefaRepository.save(tarefa);
        return new TarefaDto(tarefa);
    }

}
