package br.com.brunno.tarefas.user;

public record LoginUserDto(
        String email,
        String password
) {
}
