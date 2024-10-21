package br.com.brunno.tarefas.tarefa;

public record TarefaDto(
        Long id,
        String titulo,
        String descricao,
        String estado
)
{
    public TarefaDto(Tarefa tarefa) {
        this(
                tarefa.getId(),
                tarefa.getTitulo(),
                tarefa.getDescricao(),
                tarefa.getEstado().name()
        );
    }
}
