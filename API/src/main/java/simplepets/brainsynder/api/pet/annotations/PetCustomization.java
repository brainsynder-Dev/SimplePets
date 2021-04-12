package simplepets.brainsynder.api.pet.annotations;

import lib.brainsynder.sounds.SoundMaker;
import simplepets.brainsynder.api.pet.PetWeight;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface PetCustomization {
    SoundMaker ambient ();
    PetWeight weight () default PetWeight.NONE;
}