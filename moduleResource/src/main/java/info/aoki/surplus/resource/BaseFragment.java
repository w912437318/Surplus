package info.aoki.surplus.resource;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * <h3>Base fragment</h3>
 * <p>Create time:11/30/18 9:36 PM</p>
 * <ol>
 * <li><pre>getLayoutId()</pre></li>
 * <li><pre>initData()</pre></li>
 * <li><pre>bindWidget()</pre></li>
 * <li><pre>initView()</pre></li>
 * <li><pre>initPresenter()</pre></li>
 * <li><pre>fetchData()</pre></li>
 * </ol>
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
        initData();
    }

    @Nullable
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayoutResId(), container, false);
    }

    @Override
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findWidget(view);
        initView(view);
        this.mPresenter = initPresenter();
    }

    protected abstract int getLayoutResId();

    protected abstract void initData();

    protected abstract void findWidget(View rootView);

    protected abstract void initView(View rootView);

    protected abstract T initPresenter();

    @Override
    public void onStart() {
        super.onStart();
        fetchData();
    }

    protected abstract void fetchData();
}
