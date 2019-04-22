package com.karnavauli.app.validators;

import com.karnavauli.app.model.dto.CustomerDto;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class CustomerValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(CustomerDto.class);
    }

    @Override
    public void validate(Object o, Errors errors) {

        CustomerDto customerDto = (CustomerDto) o;

        String mailRegex = ".+@.+\\..+";

        /*if(!customerDto.getMail().matches(mailRegex)){
            errors.rejectValue("mail", "wrong mail");
        }*/

    }
}
