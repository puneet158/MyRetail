package com.retail.myretail;

import com.retail.myretail.exception.EntityNotFoundException;
import com.retail.myretail.message.I18nMessageCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller exception handler.
 * <p/>
 * Serves to centralize exception handling and formatting of error messages.
 *
 * @see ControllerAdvice
 */
@ControllerAdvice
public class ApiControllerAdvice {

    @Inject
    private MessageSourceAccessor messageSourceAccessor;

    private void populateResponseMap(Map<String, Object> map, HttpServletRequest request, String message) {
        map.put("path", request.getRequestURI());
        if (StringUtils.isNotBlank(message)) {
            map.put("message", message);
        }
    }

    @ResponseBody
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleEntityNotFoundException(HttpServletRequest request, EntityNotFoundException e) {

        Map<String, Object> map = new HashMap();

        populateResponseMap(map, request, e.getMessage() == null ? I18nMessageCode.NOTFOUND : e.getMessage());
        return new ResponseEntity(map, HttpStatus.NOT_FOUND);
    }

     @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValidException(HttpServletRequest request,
                                                                                     MethodArgumentNotValidException exception) {

        Map<String, Object> map = processBindingResult(exception.getBindingResult());
        populateResponseMap(map, request, "");
        return new ResponseEntity(map, HttpStatus.BAD_REQUEST);
    }

    private Map<String, Object> processBindingResult(BindingResult bindingResult) {
        Map<String, Object> map = new HashMap();
        map.put("target", bindingResult.getTarget());
        map.put("fieldErrors", processFieldErrors(bindingResult.getFieldErrors()));
        return map;
    }

    private List<Map<String, Object>> processFieldErrors(List<FieldError> fieldErrors) {
        List<Map<String, Object>> errors = new ArrayList();
        for (FieldError fieldError : fieldErrors) {
            Map<String, Object> error = new HashMap();
            error.put("field", fieldError.getField());
            error.put("rejectedValue", fieldError.getRejectedValue());
            error.put("message", messageSourceAccessor.getMessage(fieldError));
            errors.add(error);
        }
        return errors;
    }
}
