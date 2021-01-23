package simplepets.brainsynder.utils;

public interface Call<T> {
    void call(T data);

    default void onFail() {}
}