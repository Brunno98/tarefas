package br.com.brunno.tarefas.tarefa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TarefaRepository extends JpaRepository<Tarefa, Long> {
    List<Tarefa> findByResponsavel_Email(String username);
}
