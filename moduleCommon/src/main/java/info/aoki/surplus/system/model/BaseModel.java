package info.aoki.surplus.system.model;

import android.content.Context;

import info.aoki.surplus.system.GreenDaoUtil;

/**
 * <h3>Base Model</h3>
 * <p>Create time:12/4/18 3:13 PM</p>
 *
 * @author wong
 * @version 1.0
 */
public abstract class BaseModel {
    protected DaoSession mDaoSession;
    protected Context mContext;

    public BaseModel(Context context) {
        this.mContext = context;
        this.mDaoSession = GreenDaoUtil.getGreenDaoUtil().openDaoSession();
    }

}
