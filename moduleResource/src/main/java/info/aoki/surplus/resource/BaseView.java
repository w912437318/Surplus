package info.aoki.surplus.resource;

public interface BaseView {
    public default void showHint(String message) {
    }

    public default void showHint(int resourceId) {
    }

}
