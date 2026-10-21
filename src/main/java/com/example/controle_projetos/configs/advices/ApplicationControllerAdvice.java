package com.example.controle_projetos.configs.advices;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.example.controle_projetos.configs.exceptions.RegraNegocioException;
import com.example.controle_projetos.dtos.ApiErrorDTO;

@RestControllerAdvice
public class ApplicationControllerAdvice {

    @ExceptionHandler(RegraNegocioException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorDTO handleRegraNegocioException(RegraNegocioException ex) {
        return new ApiErrorDTO(ex.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorDTO handleJsonInvalido(HttpMessageNotReadableException ex) {
        return new ApiErrorDTO("Dados enviados em formato inválido. Use datas no formato yyyy-MM-dd");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorDTO handleParametroInvalido(MethodArgumentTypeMismatchException ex) {
        return new ApiErrorDTO("Valor inválido para o parâmetro " + ex.getName());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorDTO handleParametroAusente(MissingServletRequestParameterException ex) {
        return new ApiErrorDTO("Parâmetro obrigatório não informado: " + ex.getParameterName());
    }
}
