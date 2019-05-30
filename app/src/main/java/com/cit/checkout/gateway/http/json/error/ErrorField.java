package com.cit.checkout.gateway.http.json.error;

import com.cit.checkout.domain.error.ConstraintError;
import lombok.*;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.validation.BindingResult;

import javax.validation.ConstraintViolation;
import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.newArrayList;

@ToString
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = { "message", "fields" })
public class ErrorField {

    private String messageCode;
    private String message;
    private List<String> fields;

    public static List<ErrorField> create(final BindingResult result) {
        final List<org.springframework.validation.FieldError> fieldErrors = result.getFieldErrors();

        Map<ErrorField, List<String>> fieldsMap = new HashMap<>();
        fieldErrors.forEach(
                fieldError ->
                        fieldsMap
                                .computeIfAbsent(ErrorField.builder()
                                        .messageCode(fieldError.getCode())
                                        .message(fieldError.getDefaultMessage())
                                        .build(), fields -> new ArrayList<>())
                                .add(fieldError.getField()));

        List<ErrorField> errors = new ArrayList<>();
        fieldsMap.forEach(
                (errorField, fields) -> {
                    errorField.setFields(fields);
                    errors.add(errorField);
                });

        return errors;
    }

    public static List<ErrorField> create(final Set<ConstraintViolation<?>> validationErrors) {
        Map<String, Pair<String, List<String>>> fieldsErrors = getFieldErrosMap(validationErrors);

        List<ErrorField> errors = new ArrayList<>();

        fieldsErrors.forEach(
                (messageTemplate, pair) ->
                        errors.add(new ErrorField(messageTemplate, pair.getLeft(), pair.getRight())));
        return errors;
    }

    /**
     * Map que retorna a key sendo o messageCode e um objeto Pair de lado esquerdo uma mensagem de erro explicativa e
     * de lado direito sendo uma lista com os campos que ocorreram o mesmo tipo de erro
     *
     * @param validationErrors
     * @return
     */
    private static Map<String, Pair<String, List<String>>> getFieldErrosMap(Set<ConstraintViolation<?>> validationErrors) {
        Map<String, Pair<String, List<String>>> fieldsErrors = new HashMap<>();

        validationErrors.forEach(
                constraintViolation -> {
                    fieldsErrors
                            .computeIfAbsent(constraintViolation.getMessageTemplate(), (messageTemplate) ->
                                    new MutablePair<>(constraintViolation.getMessage(), new ArrayList<>())
                            ).getRight().add(constraintViolation.getPropertyPath().toString());
                });

        return fieldsErrors;
    }

    public static List<ErrorField> create(List<ConstraintError> constraintsErrors) {
        return constraintsErrors.stream()
                .collect(Collectors.groupingBy(constraintError -> ErrorField.builder()
                        .messageCode(constraintError.getMessageTemplate())
                        .message(constraintError.getMessage())
                        .fields(newArrayList())
                        .build(),
                        LinkedHashMap::new,
                        Collectors.mapping(ConstraintError::getPropertyPath, Collectors.toList())))
                .entrySet()
                .stream()
                .peek(entry -> entry.getKey().setFields(entry.getValue()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}
