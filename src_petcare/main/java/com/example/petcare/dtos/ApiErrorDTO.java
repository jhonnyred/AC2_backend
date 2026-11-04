package com.example.petcare.dtos;

import java.util.List;

public class ApiErrorDTO {
    private List<String> errors;

    public ApiErrorDTO(String error) {
        this.errors = List.of(error);
    }

    public ApiErrorDTO(List<String> errors) {
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }
}
