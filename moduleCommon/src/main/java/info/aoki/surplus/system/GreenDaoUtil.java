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
    /**
     * Application Database Name
     */
    private final static String RECORD_DB_NAME = "record_db";
    private static GreenDaoUtil sGreenDaoUtil;
    private DaoMaster mDaoMaster;
    private DaoMaster.DevOpenHelper mDevOpenHelper;

    /**
     * Constructor
     *
     * @param context {@link Context}
     */
    private GreenDaoUtil(Context context) {
        this.mDevOpenHelper = new DaoMaster.DevOpenHelper(context, RECORD_DB_NAME);
        this.mDaoMaster = new DaoMaster(mDevOpenHelper.getWritableDb());
    }

    /**
     * <p>GreenDaoUtil will created in this method.</p>
     * <strong>This method shouldn't modify and call by hand.</strong>
     *
     * @param context {@link Context}
     */
    public static void createDaoUtil(Context context) {
        if (sGreenDaoUtil == null) {
            synchronized (GreenDaoUtil.class) {
                if (sGreenDaoUtil == null) {

                    sGreenDaoUtil = new GreenDaoUtil(context);
                }
            }
        }
    }

    /**
     * <h3>Get GreenDaoUtil Instance</h3>
     * <p>Use this {@link GreenDaoUtil} instance, you can get DaoSession by {@link GreenDaoUtil#openDaoSession()}</p>
     *
     * @return {@link GreenDaoUtil}
     */
    public static GreenDaoUtil getGreenDaoUtil() {
        if (sGreenDaoUtil == null)
            throw new NullPointerException("You didn't call createDaoUtil() in ApplicationContext");
        return sGreenDaoUtil;
    }

    /**
     * <h3>Get Dao Session</h3>
     * <p><strong>Note:</strong></p>
     * <p>Get database session by this method</p>
     * <p>Please <strong>release</strong> DaoSession when you finish database operation</p>
     *
     * @return DaoSession
     */
    public DaoSession openDaoSession() {
        return mDaoMaster.newSession();
    }

    public void close() {
        this.mDevOpenHelper.close();
    }
}
