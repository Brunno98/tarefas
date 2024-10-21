package br.com.brunno.tarefas.user;

public record CreateUserDto(
        String email,
        String password,
        RoleName role
) {
}