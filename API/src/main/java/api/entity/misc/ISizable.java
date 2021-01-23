package api.entity.misc;

public interface ISizable {
    int getSize();

    void setSize(int i);

    default boolean isSmall() {
        return getSize() <= 1;
    }
}
