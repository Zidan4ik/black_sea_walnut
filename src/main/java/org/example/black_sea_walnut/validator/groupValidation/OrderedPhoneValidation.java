package org.example.black_sea_walnut.validator.groupValidation;

import jakarta.validation.GroupSequence;

@GroupSequence({
        PhoneValidGroups.NotBlankCheck.class,
        PhoneValidGroups.NotLength.class,
        PhoneValidGroups.NotPhoneFormatValidation.class})
public interface OrderedPhoneValidation {
}
