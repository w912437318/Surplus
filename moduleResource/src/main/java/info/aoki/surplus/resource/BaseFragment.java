package info.aoki.surplus.resource;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * <h3>Base fragment</h3>
 * <p>Create time:11/30/18 9:36 PM</p>
 *
 * @author wong
 * @version 1.0
 */
public abstract class BaseFragment<T extends BasePresenter> extends Fragment {
    protected T mPresenter;
    protected Context mContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = getContext();
        this.mPresenter = initPresenter();
        initData();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayoutResId(), container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findWidget(view);
        initView(view);
    }

    /**
     * Find widget method, this method will run in {@link BaseFragment {@link #initView(View)}}
     * <p>So, if you want to override <code>initView(rootView)</code>, you need call super method!</p>
     *
     * @param rootView Fragment View
     */
    protected abstract void findWidget(View rootView);

    /**
     * <p>Init presenter in here.</p>
     *
     * @return Presenter {@link BasePresenter}
     */
    protected abstract T initPresenter();

    /**
     * <p>Init data will be needed</p>
     * <p>This method will be called in onCreate() method.</p>
     */
    protected abstract void initData();

    /**
     * <p>Require layout resource id</p>
     *
     * @return Layout resource id
     */
    protected abstract int getLayoutResId();

    /**
     * <p>Create widget,set listener, set adapter in this method.</p>
     * <p>This method will be called in <code>onCreateView()</code> method.</p>
     */
    protected abstract void initView(View rootView);
}
