package com.example.petcare.configs.advices;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.example.petcare.configs.exceptions.RecursoNaoEncontradoException;
import com.example.petcare.configs.exceptions.RegraNegocioException;
import com.example.petcare.dtos.ApiErrorDTO;

@RestControllerAdvice
public class ApplicationControllerAdvice {

    // Regras de negócio violadas -> 400
    @ExceptionHandler(RegraNegocioException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorDTO handleRegraNegocioException(RegraNegocioException ex) {
        return new ApiErrorDTO(ex.getMessage());
    }

    // Registro não encontrado -> 404
    @ExceptionHandler(RecursoNaoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorDTO handleRecursoNaoEncontrado(RecursoNaoEncontradoException ex) {
        return new ApiErrorDTO(ex.getMessage());
    }

    // Conflito de dados, ex.: CPF ou CRMV duplicado -> 409
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiErrorDTO handleDataIntegrity(DataIntegrityViolationException ex) {
        return new ApiErrorDTO("Violação de integridade dos dados (registro duplicado ou em uso)");
    }

    // JSON mal formatado, data inválida ou espécie inexistente -> 400
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorDTO handleJsonInvalido(HttpMessageNotReadableException ex) {
        return new ApiErrorDTO("JSON inválido. Verifique os campos, datas (yyyy-MM-dd / yyyy-MM-ddTHH:mm:ss) e espécies");
    }

    // Parâmetro de URL com tipo errado, ex.: /api/tutor/abc -> 400
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorDTO handleTipoParametro(MethodArgumentTypeMismatchException ex) {
        return new ApiErrorDTO("Parâmetro inválido: " + ex.getName());
    }
}
