package org.example.black_sea_walnut.validator.groupValidation;

import jakarta.validation.GroupSequence;

@GroupSequence({EmailValidGroups.NotBlankCheck.class, EmailValidGroups.EmailCheck.class, EmailValidGroups.EmailExistenceCheck.class})
public interface OrderedEmailValidation {
}
