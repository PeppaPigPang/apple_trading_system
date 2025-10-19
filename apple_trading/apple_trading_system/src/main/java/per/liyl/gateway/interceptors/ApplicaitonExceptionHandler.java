package per.liyl.gateway.interceptors;

import com.alibaba.fastjson2.JSON;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import per.liyl.exception.ApplicationException;
import per.liyl.enums.ApplicationExceptionEnum;
import per.liyl.gateway.response.Response;

@ControllerAdvice
public class ApplicaitonExceptionHandler {

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<String> handleBusinessException(ApplicationException ex, WebRequest request) {
        Response error = new Response(
                ex.getErrorCode(),
                ex.getErrorMsg()
        );
        return new ResponseEntity<>(JSON.toJSONString(error), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response> handleException(Exception ex, WebRequest request) {
        Response error = new Response(
                ApplicationExceptionEnum.SYSTEM_ERROR.getErrorCode(),
                ex.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
