package org.example.black_sea_walnut.validator.groupValidation;

import jakarta.validation.GroupSequence;

@GroupSequence({
        PasswordValidGroups.NotBlankCheck.class,
        PasswordValidGroups.NotMatchCurrentPasswordValidation.class,
        PasswordValidGroups.NotBlankNew.class,
        PasswordValidGroups.NotBlankConfirm.class})
public interface OrderedPasswordValidation {
}
