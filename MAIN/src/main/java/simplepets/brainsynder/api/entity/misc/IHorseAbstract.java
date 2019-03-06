package simplepets.brainsynder.api.entity.misc;

public interface IHorseAbstract extends IAgeablePet, IFlag {
    boolean isSaddled();

    void setSaddled(boolean var1);
}
