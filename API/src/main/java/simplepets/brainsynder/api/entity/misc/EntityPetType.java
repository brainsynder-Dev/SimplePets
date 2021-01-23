package simplepets.brainsynder.api.entity.misc;

import simplepets.brainsynder.api.pet.PetType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EntityPetType{
	PetType petType();
}