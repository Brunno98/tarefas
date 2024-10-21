package br.com.brunno.tarefas.tarefa;

import br.com.brunno.tarefas.user.User;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record NovaTarefaDto(
        @NotBlank @Length(max = 255) String titulo,
        @NotBlank @Length(max = 1000) String descricao
) {
    public Tarefa novaTarefa(User responsavel) {
        return Tarefa.novaTarefa(titulo, descricao, responsavel);
    }
}
