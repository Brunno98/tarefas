package br.com.brunno.tarefas.tarefa;

import br.com.brunno.tarefas.user.Role;
import br.com.brunno.tarefas.user.RoleName;
import br.com.brunno.tarefas.user.User;
import br.com.brunno.tarefas.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class TarefaControllerTest {
    public static final String USERNAME = "test@email.com";
    public static final String OTHER_USERNAME = "other@email.com";
    public static final long TAREFA_ID = 1L;

    @InjectMocks
    TarefaController tarefaController;
    @Mock
    UserRepository userRepository;
    @Mock
    TarefaRepository tarefaRepository;

    @Test
    @DisplayName("Ao criar uma tarefa, caso o usuario não exista, deve ser lancada uma excecao")
    void usuarioNaoExisteAoCriarNovaTarefa() {
        doReturn(Optional.empty()).when(userRepository).findByEmail(USERNAME);

        assertThrows(RuntimeException.class,
                () -> tarefaController.criaNovaTarefa(
                        new NovaTarefaDto("foo", "bar"), USERNAME)
        );
    }

    @Test
    @DisplayName("Ao excluir uma tarefa, caso a tarefa não existe, deve ser lancado um IllegaArgumentException")
    void excluirTarefaNaoExistente() {
        doReturn(Optional.empty()).when(tarefaRepository).findById(TAREFA_ID);

        assertThrows(IllegalArgumentException.class, () -> tarefaController.excluiTarefa(TAREFA_ID, USERNAME));
    }

    @Test
    @DisplayName("Caso a terafa a ser excluida não pertenca ao usuario que solicitou, uma exceção deve ser lancada")
    void excluirTarefaDeOutroUsuario() {
        User user = new User(USERNAME, "test", List.of(new Role(RoleName.ROLE_CUSTOMER), new Role(RoleName.ROLE_ADMINISTRATOR)));
        Tarefa tarefa = Tarefa.novaTarefa("foo", "bar", user);
        doReturn(Optional.of(tarefa)).when(tarefaRepository).findById(TAREFA_ID);

        assertThrows(RuntimeException.class, () -> tarefaController.excluiTarefa(TAREFA_ID, OTHER_USERNAME));
    }

    @Test
    @DisplayName("Alterar o estado de uma tarefa que nao existe deve lancar uma excecao")
    void alteraEstadoDeTarefaNaoExistente() {
        doReturn(Optional.empty()).when(tarefaRepository).findById(TAREFA_ID);

        assertThrows(IllegalArgumentException.class, () -> tarefaController.alteraEstado(TAREFA_ID, Estado.CONCLUIDO, USERNAME));
    }

    @Test
    @DisplayName("Tentar alterar o estado da tarefa de outro usuario deve lancar excecao")
    void alterarEstadoDeTarefaDeOutroUsuario() {
        User user = new User(USERNAME, "test", List.of(new Role(RoleName.ROLE_CUSTOMER), new Role(RoleName.ROLE_ADMINISTRATOR)));
        Tarefa tarefa = Tarefa.novaTarefa("foo", "bar", user);
        doReturn(Optional.of(tarefa)).when(tarefaRepository).findById(TAREFA_ID);

        assertThrows(RuntimeException.class, () -> tarefaController.alteraEstado(TAREFA_ID, Estado.CONCLUIDO, OTHER_USERNAME));
    }

    @Test
    @DisplayName("Alterar uma tarefa que nao existe deve lancar uma excecao")
    void alteraTarefaNaoExistente() {
        doReturn(Optional.empty()).when(tarefaRepository).findById(TAREFA_ID);

        assertThrows(IllegalArgumentException.class, () -> tarefaController.alteraTarefa(TAREFA_ID, new AlteraTarefaDto("descricao"), USERNAME));
    }

    @Test
    @DisplayName("Tentar alterar a tarefa de outro usuario deve lancar excecao")
    void alterarTarefaDeOutroUsuario() {
        User user = new User(USERNAME, "test", List.of(new Role(RoleName.ROLE_CUSTOMER), new Role(RoleName.ROLE_ADMINISTRATOR)));
        Tarefa tarefa = Tarefa.novaTarefa("foo", "bar", user);
        doReturn(Optional.of(tarefa)).when(tarefaRepository).findById(TAREFA_ID);

        assertThrows(RuntimeException.class, () -> tarefaController.alteraTarefa(TAREFA_ID, new AlteraTarefaDto("descricao") ,OTHER_USERNAME));
    }
}