package com.karnavauli.app.controllers;

import com.karnavauli.app.exceptions.MyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ExceptionsController {

    @ExceptionHandler(MyException.class)
    public String myExceptionHandler(MyException e, Model model) {
        log.error(e.getExceptionInfo().getMessage());
        model.addAttribute("message", e.getExceptionInfo().getCode().getDescription());
        return "errors/myErrorPage";
    }

}
