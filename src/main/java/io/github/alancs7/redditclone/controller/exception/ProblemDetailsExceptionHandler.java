package io.github.alancs7.redditclone.controller.exception;

import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;
import io.github.alancs7.redditclone.exception.RedditCloneException;
import io.github.alancs7.redditclone.exception.ResourceNotFoundException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ProblemDetailsExceptionHandler extends ResponseEntityExceptionHandler {

    public static final String MSG_GENERIC_ERROR_USER = "An unexpected internal system error has occurred. " +
            "Try again and if the problem persists, " +
            "contact your system administrator.";

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleBadCredentialsException(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return ResponseEntity.status(status).headers(headers).build();
    }

    @ExceptionHandler(PropertyReferenceException.class)
    public ResponseEntity<Object> handlePropertyReferenceException(PropertyReferenceException ex, WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String propertyName = ex.getPropertyName();
        String className = ex.getType().getType().getSimpleName();

        String detail = String.format("The URL parameter '%s' does not exist for the type '%s'." +
                "Correct and enter a correct value.", propertyName, className);

        ProblemDetails apiError = createStandardErrorBuilder(status.value(), detail)
                .userMessage(MSG_GENERIC_ERROR_USER)
                .build();

        return handleExceptionInternal(ex, apiError, new HttpHeaders(), status, request);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return handleValidationInternal(ex, headers, status.value(), request, ex.getBindingResult());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return handleValidationInternal(ex, headers, status.value(), request, ex.getBindingResult());
    }

    private ResponseEntity<Object> handleValidationInternal(Exception ex, HttpHeaders headers, int status, WebRequest request, BindingResult bindingResult) {
        String detail = "One or more fields are invalid. Fill them in and try again.";

        List<ProblemDetails.Field> fieldErrors = bindingResult.getAllErrors().stream()
                .map(objectError -> {
                    String message = messageSource.getMessage(objectError, LocaleContextHolder.getLocale());

                    String name = objectError.getObjectName();

                    if (objectError instanceof FieldError fieldError) name = fieldError.getField();

                    return ProblemDetails.Field.builder()
                            .name(name)
                            .userMessage(message)
                            .build();
                })
                .collect(Collectors.toList());

        ProblemDetails apiError = createStandardErrorBuilder(status, detail)
                .userMessage(detail)
                .fields(fieldErrors)
                .build();

        return handleExceptionInternal(ex, apiError, headers, HttpStatusCode.valueOf(status), request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleUncaught(Exception ex, WebRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String detail = MSG_GENERIC_ERROR_USER;

        ex.printStackTrace();

        ProblemDetails apiError = createStandardErrorBuilder(status.value(), detail)
                .userMessage(detail)
                .build();

        return handleExceptionInternal(ex, apiError, new HttpHeaders(), status, request);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String requestURL = ex.getRequestURL();

        String detail = String.format("The %s resource you tried to access is non-existent.", requestURL);

        ProblemDetails apiError = createStandardErrorBuilder(status.value(), detail)
                .userMessage(MSG_GENERIC_ERROR_USER)
                .build();

        return handleExceptionInternal(ex, apiError, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        if (ex instanceof MethodArgumentTypeMismatchException) {
            return handleMethodArgumentTypeMismatchException((MethodArgumentTypeMismatchException) ex, headers, (HttpStatus) status, request);
        }
        return super.handleTypeMismatch(ex, headers, status, request);
    }

    private ResponseEntity<Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String propertyName = ex.getName();
        String invalidValue = ex.getValue().toString();
        String requiredType = ex.getRequiredType().getSimpleName();

        String detail = String.format("The URL parameter '%s' received a value of '%s', which is of an invalid type. " +
                "Correct and enter a value compatible with the %s type.", propertyName, invalidValue, requiredType);

        ProblemDetails apiError = createStandardErrorBuilder(status.value(), detail)
                .userMessage(MSG_GENERIC_ERROR_USER)
                .build();

        return handleExceptionInternal(ex, apiError, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Throwable rootCause = ExceptionUtils.getRootCause(ex);

        if (rootCause instanceof InvalidFormatException) {
            return handleInvalidFormatException((InvalidFormatException) rootCause, headers, (HttpStatus) status, request);
        } else if (rootCause instanceof PropertyBindingException) {
            return handlePropertyBindingException((PropertyBindingException) rootCause, headers, (HttpStatus) status, request);
        }

        String detail = "The request body is invalid. Check for a syntax error.";

        ProblemDetails apiError = createStandardErrorBuilder(status.value(), detail)
                .detail(MSG_GENERIC_ERROR_USER)
                .build();

        return handleExceptionInternal(ex, apiError, headers, status, request);
    }

    private ResponseEntity<Object> handlePropertyBindingException(PropertyBindingException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String propertyName = joinPropertyName(ex.getPath());

        String detail = String.format("The property '%s' does not exist. "
                + "Correct or remove this property and try again.", propertyName);

        ProblemDetails apiError = createStandardErrorBuilder(status.value(), detail)
                .userMessage(MSG_GENERIC_ERROR_USER)
                .build();

        return handleExceptionInternal(ex, apiError, headers, status, request);
    }

    private ResponseEntity<Object> handleInvalidFormatException(InvalidFormatException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String propertyName = joinPropertyName(ex.getPath());
        String invalidValue = ex.getValue().toString();
        String expectedType = ex.getTargetType().getSimpleName();

        String detail = String.format("The property '%s' received the value '%s', which is of an invalid type. " +
                "Correct and enter a value compatible with the %s type.", propertyName, invalidValue, expectedType);

        ProblemDetails apiError = createStandardErrorBuilder(status.value(), detail)
                .userMessage(MSG_GENERIC_ERROR_USER)
                .build();

        return handleExceptionInternal(ex, apiError, headers, status, request);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        String detail = ex.getMessage();

        ProblemDetails apiError = createStandardErrorBuilder(status.value(), detail)
                .userMessage(detail)
                .build();

        return handleExceptionInternal(ex, apiError, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(RedditCloneException.class)
    public ResponseEntity<?> handleRedditCloneException(RedditCloneException ex, WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String detail = ex.getMessage();

        ProblemDetails apiError = createStandardErrorBuilder(status.value(), detail)
                .userMessage(detail)
                .build();

        return handleExceptionInternal(ex, apiError, new HttpHeaders(), status, request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        HttpStatus httpStatus = HttpStatus.valueOf(statusCode.value());

        if (body == null || body instanceof String) {
            body = ProblemDetails.builder()
                    .title(body instanceof String error ? error : httpStatus.getReasonPhrase())
                    .status(statusCode.value())
                    .userMessage(MSG_GENERIC_ERROR_USER)
                    .timestamp(OffsetDateTime.now())
                    .build();
        }

        return super.handleExceptionInternal(ex, body, headers, httpStatus, request);
    }

    private ProblemDetails.ProblemDetailsBuilder createStandardErrorBuilder(int status, String detail) {
        return ProblemDetails.builder()
                .status(status)
                .detail(detail)
                .timestamp(OffsetDateTime.now());
    }

    private String joinPropertyName(List<Reference> references) {
        return references.stream()
                .map(Reference::getFieldName)
                .collect(Collectors.joining("."));
    }

}