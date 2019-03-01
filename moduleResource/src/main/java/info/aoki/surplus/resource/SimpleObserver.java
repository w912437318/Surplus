package info.aoki.surplus.resource;

import rx.Observer;

public abstract class SimpleObserver<T> implements Observer<T> {

    @Override
    public void onCompleted() {

    }

    /**
     * Default error method
     * <hr/>
     * Print error message to stack trace
     * @param e Error message
     */
    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
    }
}
