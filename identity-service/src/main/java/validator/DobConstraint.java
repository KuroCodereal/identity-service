package validator;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;


// validate cho field
@Target({ElementType.FIELD})

//annotation duoc xu ly luc nao
//lombock la cac annotation xu ly runtime
@Retention(RetentionPolicy.RUNTIME)
//class chiu trach nhiem validate cho annotation nay
@Constraint(validatedBy = {DobValidator.class})
@Inherited
@Documented


// ban than cai nay chi la khai bao con xu ly thi can 1 cai validator xu ly
public @interface DobConstraint {
    String message() default "Invalid date of birth";

    int min();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
