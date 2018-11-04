package com.karnavauli.app.validators;

import com.karnavauli.app.model.dto.TicketDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class TicketValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(TicketDto.class);
    }

    @Override
    public void validate(Object o, Errors errors) {

    }
}
