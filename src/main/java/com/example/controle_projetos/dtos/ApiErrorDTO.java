package com.example.controle_projetos.dtos;

import java.util.List;

public class ApiErrorDTO {
    private List<String> errors;

    public ApiErrorDTO(String error) {
        this.errors = List.of(error);
    }

    public List<String> getErrors() {
        return errors;
    }
}
