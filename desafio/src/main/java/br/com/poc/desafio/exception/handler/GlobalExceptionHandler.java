package br.com.poc.desafio.exception.handler;

import java.net.URI;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import br.com.poc.desafio.domain.response.ErrorResponse;
import br.com.poc.desafio.exception.core.BusinessException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {

        List<ErrorResponse> errors = ex.getBindingResult().getFieldErrors().stream()
            .map(fieldError -> new ErrorResponse(fieldError.getField(), fieldError.getDefaultMessage()))
            .collect(Collectors.toList());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
            "The following errors occurred:");
        problemDetail.setType(URI.create("about:blank"));
        problemDetail.setTitle("Validation failed");
        problemDetail.setStatus(HttpStatus.BAD_REQUEST.value());
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setProperty("errors", errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ProblemDetail> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {

        final var errorResponse = ErrorResponse.builder()
            .mensagemError("Requisição inválida")
            .build();

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
            "The following errors occurred:");
        problemDetail.setType(URI.create("about:blank"));
        problemDetail.setTitle("Invalid request");
        problemDetail.setStatus(HttpStatus.BAD_REQUEST.value());
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setProperty("errors", Collections.singletonList(errorResponse));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ProblemDetail> handleBusinessException(BusinessException ex) {

        final var errorResponse = ErrorResponse.builder()
            .mensagemError(ex.getMessage())
            .build();

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY,
            "The following errors occurred:");
        problemDetail.setTitle(HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase());
        problemDetail.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setProperty("errors", Collections.singletonList(errorResponse));

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(problemDetail);
    }
}
