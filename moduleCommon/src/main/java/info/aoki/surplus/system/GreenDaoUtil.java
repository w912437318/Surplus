package info.aoki.surplus.system;

import android.content.Context;

import info.aoki.surplus.system.model.DaoMaster;
import info.aoki.surplus.system.model.DaoSession;

/**
 * <h3>GreenDaoUtil</h3>
 * <p>Create time:12/4/18 2:45 PM</p>
 *
 * @author wong
 * @version 1.0
 */
public class GreenDaoUtil {
    private final static String RECORD_DB_NAME = "record_db";
    private static GreenDaoUtil sGreenDaoUtil;
    private DaoMaster mDaoMaster;
    private DaoMaster.DevOpenHelper mDevOpenHelper;

    private GreenDaoUtil(Context context) {
        this.mDevOpenHelper = new DaoMaster.DevOpenHelper(context, RECORD_DB_NAME);
        this.mDaoMaster = new DaoMaster(mDevOpenHelper.getWritableDb());
    }

    public static void createDaoUtil(Context context) {
        if (sGreenDaoUtil == null) {
            synchronized (GreenDaoUtil.class) {
                if (sGreenDaoUtil == null) {

                    sGreenDaoUtil = new GreenDaoUtil(context);
                }
            }
        }
    }

    public static GreenDaoUtil getGreenDaoUtil() {
        if (sGreenDaoUtil == null)
            throw new NullPointerException("You didn't call createDaoUtil() in ApplicationContext");
        return sGreenDaoUtil;
    }

    public DaoSession openDaoSession() {
        return mDaoMaster.newSession();
    }

    public void close() {
        this.mDevOpenHelper.close();
    }
}
