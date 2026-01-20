    package org.example.black_sea_walnut.validator.validator;

    import jakarta.validation.ConstraintValidator;
    import jakarta.validation.ConstraintValidatorContext;
    import org.example.black_sea_walnut.validator.annotation.PhoneFormatValidation;

    import java.util.regex.Pattern;

    public class PhoneFormatValidator implements ConstraintValidator<PhoneFormatValidation, String> {
        private static final Pattern PHONE_PATTERN = Pattern.compile(
                "^\\+380\\d{9}$");
        @Override
        public boolean isValid(String phone, ConstraintValidatorContext context) {
            if (phone == null || phone.isBlank()) {
                return true;
            }
            return PHONE_PATTERN.matcher(phone).matches();
        }
    }
