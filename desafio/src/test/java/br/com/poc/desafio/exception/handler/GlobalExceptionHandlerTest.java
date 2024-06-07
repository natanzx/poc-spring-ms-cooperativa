package br.com.poc.desafio.exception.handler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import br.com.poc.desafio.exception.core.BusinessException;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

    @Test
    void handleMethodArgumentNotValidException() {
        // given
        final MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        final BindingResult bindingResult = mock(BindingResult.class);

        // when
        when(bindingResult.getFieldErrors())
            .thenReturn(List.of(new FieldError("station", "name", "should not be empty")));
        when(exception.getBindingResult()).thenReturn(bindingResult);

        final ResponseEntity<ProblemDetail> response = globalExceptionHandler.handleMethodArgumentNotValidException(
            exception);

        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertInstanceOf(ProblemDetail.class, response.getBody());
    }

    @Test
    void handleHttpMessageNotReadableException() {
        // given
        final HttpMessageNotReadableException exception = mock(HttpMessageNotReadableException.class);

        final ResponseEntity<ProblemDetail> response = globalExceptionHandler.handleHttpMessageNotReadableException(
            exception);

        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertInstanceOf(ProblemDetail.class, response.getBody());
    }

    @Test
    void handleBusinessException() {
        // given
        final BusinessException exception = mock(BusinessException.class);

        final ResponseEntity<ProblemDetail> response = globalExceptionHandler.handleBusinessException(exception);

        // then
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        assertInstanceOf(ProblemDetail.class, response.getBody());
    }
}