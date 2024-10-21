package br.com.brunno.tarefas.tarefa;

public record TarefaCriadaDto(
        Long id,
        String titulo,
        String descricao,
        String estado,
        String responsavel
) {
    public TarefaCriadaDto(Tarefa tarefa) {
        this(
                tarefa.getId(),
                tarefa.getTitulo(),
                tarefa.getDescricao(),
                tarefa.getEstado().name(),
                tarefa.getNomeResponsavel()
        );
    }
}
