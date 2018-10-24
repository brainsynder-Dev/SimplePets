package simplepets.brainsynder.utils;

public class TestData<T> {
    private T data;

    public TestData (T defaultData) {
        data = defaultData;
    }

    public void setData(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }
}
