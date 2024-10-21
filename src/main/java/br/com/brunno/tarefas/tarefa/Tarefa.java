package br.com.brunno.tarefas.tarefa;

import br.com.brunno.tarefas.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;

@Entity
public class Tarefa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Getter
    @Column(nullable = false)
    private String titulo;

    @Getter
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Estado estado;

    @Getter
    @Column(nullable = false, length = 1000)
    private String descricao;

    @ManyToOne(optional = false)
    private User responsavel;

    @Deprecated
    public Tarefa() {}

    public static Tarefa novaTarefa(String titulo, String descricao, User responsavel) {
        Tarefa tarefa = new Tarefa();
        tarefa.titulo = titulo;
        tarefa.descricao = descricao;
        tarefa.estado = Estado.PENDENTE;
        tarefa.responsavel = responsavel;
        return tarefa;
    }

    public String getNomeResponsavel() {
        return this.responsavel.getEmail();
    }

    public boolean temResponsavel(String username) {
        return this.responsavel.possuiUsername(username);
    }

    public void alteraEstado(Estado estado) {
        this.estado = estado;
    }

    public void atualizaTarefa(DadosAtualizacaoTarefa atualizacaoTarefa) {
        this.descricao = atualizacaoTarefa.descricao();
    }
}
