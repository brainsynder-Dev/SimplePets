package simplepets.brainsynder.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface AdditionalData {
    /**
     * This is used for the PetType classes to mark what pet is 'hostile' or 'passive'
     * @return true|false
     */
    boolean passive() default true;

    String[] otherPermissions () default "";
}