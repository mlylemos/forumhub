package com.alura.forumhub.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ── Validação de campos ────────────────────────────────────────────────────

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        fe -> fe.getDefaultMessage() != null ? fe.getDefaultMessage() : "inválido",
                        (a, b) -> a
                ));

        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setTitle("Erro de validação");
        problem.setDetail("Um ou mais campos estão inválidos.");
        problem.setType(URI.create("/errors/validation"));
        problem.setProperty("campos", errors);
        return problem;
    }

    // ── Regras de negócio ──────────────────────────────────────────────────────

    @ExceptionHandler(DuplicateTopicException.class)
    public ProblemDetail handleDuplicate(DuplicateTopicException ex) {
        ProblemDetail p = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        p.setTitle("Tópico duplicado");
        p.setDetail(ex.getMessage());
        p.setType(URI.create("/errors/duplicate-topic"));
        return p;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleNotFound(ResourceNotFoundException ex) {
        ProblemDetail p = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        p.setTitle("Recurso não encontrado");
        p.setDetail(ex.getMessage());
        p.setType(URI.create("/errors/not-found"));
        return p;
    }

    @ExceptionHandler(BusinessException.class)
    public ProblemDetail handleBusiness(BusinessException ex) {
        ProblemDetail p = ProblemDetail.forStatus(HttpStatus.FORBIDDEN);
        p.setTitle("Operação não permitida");
        p.setDetail(ex.getMessage());
        p.setType(URI.create("/errors/business"));
        return p;
    }

    // ── Segurança ──────────────────────────────────────────────────────────────

    @ExceptionHandler(BadCredentialsException.class)
    public ProblemDetail handleBadCredentials(BadCredentialsException ex) {
        ProblemDetail p = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);
        p.setTitle("Credenciais inválidas");
        p.setDetail("E-mail ou senha incorretos.");
        p.setType(URI.create("/errors/unauthorized"));
        return p;
    }

    @ExceptionHandler(AuthenticationException.class)
    public ProblemDetail handleAuthentication(AuthenticationException ex) {
        ProblemDetail p = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);
        p.setTitle("Não autenticado");
        p.setDetail("É necessário estar autenticado para acessar este recurso.");
        p.setType(URI.create("/errors/unauthorized"));
        return p;
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ProblemDetail handleAccessDenied(AccessDeniedException ex) {
        ProblemDetail p = ProblemDetail.forStatus(HttpStatus.FORBIDDEN);
        p.setTitle("Acesso negado");
        p.setDetail("Você não tem permissão para acessar este recurso.");
        p.setType(URI.create("/errors/forbidden"));
        return p;
    }

    // ── Genérico ───────────────────────────────────────────────────────────────

    @ExceptionHandler(RuntimeException.class)
    public ProblemDetail handleRuntime(RuntimeException ex) {
        ProblemDetail p = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        p.setTitle("Erro interno");
        p.setDetail(ex.getMessage());
        p.setType(URI.create("/errors/internal"));
        return p;
    }
}
