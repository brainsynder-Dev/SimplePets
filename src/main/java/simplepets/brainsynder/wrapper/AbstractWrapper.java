package simplepets.brainsynder.wrapper;

public class AbstractWrapper<T> {
    private final Class<?> type;
    private T handle;

    public AbstractWrapper(Class<?> type) {
        if (type == null) {
            throw new IllegalArgumentException("Handle type can\'t be NULL!");
        } else {
            this.type = type;
        }
    }

    public Class<?> getType() {
        return this.type;
    }

    public T getHandle() {
        return this.handle;
    }

    public void setHandle(T handle) {
        if (handle == null) {
            throw new IllegalArgumentException("Can\'t set the handle to NULL!");
        } else if (!this.type.isAssignableFrom(handle.getClass())) {
            throw new IllegalArgumentException("Handle needs to be an instance of: " + this.type.getCanonicalName());
        } else {
            this.handle = handle;
        }
    }
}
