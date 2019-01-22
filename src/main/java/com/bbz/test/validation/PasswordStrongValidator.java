package com.bbz.test.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordStrongValidator implements ConstraintValidator<PasswordStrong, String> {
    private Pattern pattern;
    private Matcher matcher;

    //TODO проверить, и поставить
    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";

    private static final String PASSWORD_PATTERN_TEST = ".{6,}";

    /*
        ^                 # start-of-string
        (?=.*[0-9])       # a digit must occur at least once
        (?=.*[a-z])       # a lower case letter must occur at least once
        (?=.*[A-Z])       # an upper case letter must occur at least once
        (?=.*[@#$%^&+=])  # a special character must occur at least once
        (?=\S+$)          # no whitespace allowed in the entire string
        .{8,}             # anything, at least eight places though
        $                 # end-of-string
     */


    @Override
    public void initialize(PasswordStrong constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return validateStrongPassword(value);
    }

    private boolean validateStrongPassword(String password) {
        pattern = Pattern.compile(PASSWORD_PATTERN_TEST);
        matcher = pattern.matcher(password);
        return matcher.matches();
    }
}