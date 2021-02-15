package meli.challenge.mutants.service.errors;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.NativeWebRequest;
import org.zalando.problem.*;
import org.zalando.problem.spring.web.advice.ProblemHandling;
import org.zalando.problem.violations.ConstraintViolationProblem;
import org.zalando.problem.violations.ConstraintViolationProblemModule;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ExceptionTranslator implements ProblemHandling {

    private static final String FIELD_ERRORS_KEY = "fieldErrors";
    private static final String MESSAGE_KEY = "message";
    private static final String PATH_KEY = "path";
    private static final String VIOLATIONS_KEY = "violations";

    @Override
    public ResponseEntity<Problem> process(@Nullable ResponseEntity<Problem> entity, NativeWebRequest request) {
        if (entity == null) return null;
        Problem problem = entity.getBody();
        if (!(problem instanceof ConstraintViolationProblem || problem instanceof DefaultProblem)) {
            return entity;
        }
        ProblemBuilder builder = Problem.builder()
                .withType(null)
                .withStatus(problem.getStatus())
                .withTitle(problem.getTitle())
                .with(PATH_KEY, request.getNativeRequest(HttpServletRequest.class).getRequestURI());

        if (problem instanceof ConstraintViolationProblem) {
            builder
                    .with(VIOLATIONS_KEY, ((ConstraintViolationProblem) problem).getViolations())
                    .with(MESSAGE_KEY, ErrorConstants.ERR_VALIDATION);
        } else {
            builder
                    .withCause(((DefaultProblem) problem).getCause())
                    .withDetail(problem.getDetail())
                    .withInstance(problem.getInstance());
            problem.getParameters().forEach(builder::with);
            if (!problem.getParameters().containsKey(MESSAGE_KEY) && problem.getStatus() != null) {
                builder.with(MESSAGE_KEY, "error.http." + problem.getStatus().getStatusCode());
            }
        }
        return new ResponseEntity<>(builder.build(), entity.getHeaders(), entity.getStatusCode());
    }

    @Override
    public ResponseEntity<Problem> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, @Nonnull NativeWebRequest request) {
        BindingResult result = ex.getBindingResult();
        List<FieldErrorVM> fieldErrors = result.getFieldErrors().stream()
                .map(f -> new FieldErrorVM(f.getObjectName().replaceFirst("DTO$", ""), f.getField(), f.getCode()))
                .collect(Collectors.toList());

        Problem problem = Problem.builder()
                .withType(null)
                .withTitle("Method argument not valid")
                .withStatus(defaultConstraintViolationStatus())
                .with(MESSAGE_KEY, ErrorConstants.ERR_VALIDATION)
                .with(FIELD_ERRORS_KEY, fieldErrors)
                .build();
        return create(ex, problem, request);
    }

    @ExceptionHandler
    public ResponseEntity<Problem> matrixBadFormedException(MatrixBadFormedException ex, NativeWebRequest request) {
        Problem problem = Problem.builder()
                .withStatus(Status.FORBIDDEN)
                .with(MESSAGE_KEY,ErrorConstants.MATRIX_BAD_FORMED_EXCEPTION)
                .withDetail(ErrorConstants.MATRIX_BAD_FORMED_EXCEPTION)
                .withTitle(ErrorConstants.MATRIX_BAD_FORMED_EXCEPTION)
                .build();
        return create(ex, problem, request);
    }

    @ExceptionHandler
    public ResponseEntity<Problem> invalidCharException(InvalidCharException ex, NativeWebRequest request) {
        Problem problem = Problem.builder()
                .withStatus(Status.FORBIDDEN)
                .with(MESSAGE_KEY,ErrorConstants.INVALID_CHAR_EXCEPTION)
                .withDetail(ex.getMessage())
                .withTitle(ex.getMessage())
                .build();
        return create(ex, problem, request);
    }

    @ExceptionHandler
    public ResponseEntity<Problem> matrixBadFormedException(MatrixBadFormedNxNException ex, NativeWebRequest request) {
        Problem problem = Problem.builder()
                .withStatus(Status.FORBIDDEN)
                .with(MESSAGE_KEY,ErrorConstants.MATRIX_BAD_FORMED_NXN_EXCEPTION)
                .withDetail(ErrorConstants.MATRIX_BAD_FORMED_NXN_EXCEPTION)
                .withTitle(ErrorConstants.MATRIX_BAD_FORMED_NXN_EXCEPTION)
                .build();
        return create(ex, problem, request);
    }


    @Override
    public boolean isCausalChainsEnabled() {
        return false;
    }
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer problemObjectMapperModules() {
        return jacksonObjectMapperBuilder -> jacksonObjectMapperBuilder.modules(
                new ProblemModule(),
                new ConstraintViolationProblemModule()
        );
    }
}
