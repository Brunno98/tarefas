package br.com.brunno.tarefas.tarefa;


import br.com.brunno.tarefas.user.JWTTokenService;
import br.com.brunno.tarefas.user.Role;
import br.com.brunno.tarefas.user.RoleName;
import br.com.brunno.tarefas.user.User;
import br.com.brunno.tarefas.user.UserAuthenticationFilter;
import br.com.brunno.tarefas.user.UserDetailsImpl;
import br.com.brunno.tarefas.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest()
@AutoConfigureMockMvc
@WithMockUser(username = "test@email.com")
@ComponentScan(excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = UserAuthenticationFilter.class))
class TarefaControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    JWTTokenService jwtTokenService;

    @Autowired
    UserRepository userRepository;

    private String authorizationToken;
    private User user = new User("test@email.com", "test", List.of(new Role(RoleName.ROLE_CUSTOMER), new Role(RoleName.ROLE_ADMINISTRATOR)));

    @BeforeEach
    void createAuthorizationToken() {
        userRepository.save(user);
        UserDetails userDetails = new UserDetailsImpl(user);
        this.authorizationToken = jwtTokenService.generateToken(userDetails);
    }

    @Test
    @DisplayName("Uma tarefa deve ser criada e, em sequencia, excluida")
    void criaEExcluiUmaTarefa() throws Exception {
        mockMvc.perform(post("/tarefas")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + authorizationToken)
                .content("""
                        {
                            "titulo": "foo",
                            "descricao": "bar"
                        }
                        """))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.titulo").value("foo"),
                        jsonPath("$.descricao").value("bar"),
                        jsonPath("$.estado").value("PENDENTE"),
                        jsonPath("$.responsavel").value("test@email.com")
                );

        mockMvc.perform(get("/tarefas")
                .header("Authorization", "Bearer "+authorizationToken))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$").isArray(),
                        jsonPath("$.length()").value(1)
                );

        mockMvc.perform(delete("/tarefas/{id}", 1L)
                .header("Authorization", "Bearer "+authorizationToken))
                .andExpect(status().isNoContent());
    }

}