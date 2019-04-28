package info.aoki.surplus.resource;

public class EventBusMessage<T> {
    private T type;
    private int code;

    public EventBusMessage(T type, int code) {
        this.type = type;
        this.code = code;
    }

    public T getType() {
        return type;
    }

    public void setType(T type) {
        this.type = type;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
