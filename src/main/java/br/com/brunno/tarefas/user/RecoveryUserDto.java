package br.com.brunno.tarefas.user;

import java.util.List;

public record RecoveryUserDto(
        Long id,
        String email,
        List<Role> roles
) {
}
