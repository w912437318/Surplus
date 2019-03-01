package info.aoki.surplus.resource;

public interface BaseView {
    public default void showToast(String message) {
    }

    public default void showToast(int resourceId) {
    }

}
