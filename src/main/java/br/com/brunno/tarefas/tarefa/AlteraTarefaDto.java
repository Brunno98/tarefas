package br.com.brunno.tarefas.tarefa;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record AlteraTarefaDto(
        @NotBlank @Length(max = 1000) String descricao
) implements DadosAtualizacaoTarefa {
}
