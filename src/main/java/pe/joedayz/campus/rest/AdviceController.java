package pe.joedayz.campus.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.hateoas.VndErrors;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class AdviceController {
    public static final String ERR_MSG_PREFIX = "errors.";

    final Logger LOG = LoggerFactory.getLogger(getClass());

    @Autowired
    MessageSource messageSource;

    @ResponseBody
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    VndErrors handleGenericExcepcion(Exception ex) {

        LOG.error(ex.getMessage(),ex);

        return new VndErrors("ERROR",
                ex.getMessage());
    }
}
