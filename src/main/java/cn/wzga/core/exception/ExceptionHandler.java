package cn.wzga.core.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import cn.wzga.core.util.Messages;
import cn.wzga.core.util.StringUtil;

public class ExceptionHandler implements HandlerExceptionResolver {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);

    final static String ERROR = "/error_page";

    @Override
    public ModelAndView resolveException(HttpServletRequest request,
                                         HttpServletResponse response, Object handler, Exception ex) {
        if (logger.isErrorEnabled()) {
            logger.error("{}", handler, ex);
        }

        Messages.setErrorMessage(ex.getMessage());
        String errorForward = (String) request.getAttribute("errorForward");

        if (!StringUtil.isBlank(errorForward)) {
            return new ModelAndView(errorForward);
        } else {
            return new ModelAndView(ERROR);
        }
    }

}
