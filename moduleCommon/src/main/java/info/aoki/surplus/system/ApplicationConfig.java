package info.aoki.surplus.system;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * <h3>Application Configuration</h3>
 * <p>Create time:12/4/18 2:02 PM</p>
 *
 * @author wong
 * @version 1.0
 */
public final class ApplicationConfig {
    /**
     * <h4>User Login Flag</h4>
     * <p><strong>Note:</strong></p>
     * <p>True: user login already</p>
     * <p>False: user didn't login yet, Use UUID replace {@link ApplicationConfig#mUsername}</p>
     */
    private boolean isUserLogin;
    /**
     * <h4>User name</h4>
     * <p><strong>Note:</strong></p>
     * <p>If {@link ApplicationConfig#isUserLogin()} be true, Set value as user account id</p>
     * <p>If {@link ApplicationConfig#isUserLogin()} be false, Set value as UUID</p>
     */
    private String mUsername;
    /**
     * <h4>Auto Backup Flag</h4>
     * <p><strong>Note:</strong></p>
     * <p>If {@link ApplicationConfig#isUserLogin()} be true, Upload record to server in background</p>
     */
    private boolean isAutoBackup;
    /**
     * <h4>Monthly Cost Limit</h4>
     * <p><strong>Note:</strong></p>
     * <p>If user set monthly cost limit, check monthly cost, its shouldn't over limit</p>
     * <p>If cost over limit, send notify to warning user</p>
     * <p>Draw limit line on Charts</p>
     */
    private float mMonthLimit;
    /**
     * <h4>Notify Flag</h4>
     * <p><strong>Note:</strong></p>
     * <p>If its be true, send notify by stratus bar, remind user note today cosed</p>
     */
    private boolean isNotify;
    /**
     * Instance of {@link ApplicationConfig}
     * <strong>Note:</strong>
     * <p>Save user custom settings</p>
     * <p>Also you can get user custom settings from this instance.</p>
     */
    @SuppressLint("StaticFieldLeak")
    private static ApplicationConfig sConfig;

    private Context mContext;

    private SharedPreferences mPreferences;

    /**
     * Constructor
     *
     * @param context {@link Context}
     */
    private ApplicationConfig(Context context) {
        this.mContext = context;

        // Create config SP
        this.mPreferences = mContext.getSharedPreferences(
                ConfigurationKeyEnum.Config.toString(),
                Context.MODE_PRIVATE);

        // Begin load configuration information from config sp
        this.loadConfiguration();
    }

    /**
     * Begin load config from SP
     */
    private void loadConfiguration() {
        ThreadPoolUtil.getThreadPoolUtil().execute(() -> {
            // Load login state flag -- Check user is login
            ApplicationConfig.this.isUserLogin = mPreferences.getBoolean(ConfigurationKeyEnum.IsUserLogin.toString(), false);

            // Load username -- if user didn't login yet, use random as user name
            mUsername = mPreferences.getString(ConfigurationKeyEnum.UserName.toString(), "");
            assert mUsername != null;
            if (mUsername.equals("") && !isUserLogin) {
                // User didn't login and it's first run application, get a new random UUID as the user name
                setUsername(UuidUtil.getRandomUUID());
            }

            // Load monthly limit -- default ¥1000
            ApplicationConfig.this.mMonthLimit = mPreferences.getFloat(ConfigurationKeyEnum.MonthLimit.toString(), 1000f);

            // Load notify flag -- Should notify user add notify
            ApplicationConfig.this.isNotify = mPreferences.getBoolean(ConfigurationKeyEnum.IsNotify.toString(), false);
        });
    }

    /**
     * <p>Application Configuration Create.</p>
     * <p>This method shouldn't be modified and called by hand</p>
     *
     * @param context {@link Context}
     */
    public static void createApplicationConfig(Context context) {
        if (sConfig == null) {
            synchronized (ApplicationConfig.class) {
                if (sConfig == null) {
                    sConfig = new ApplicationConfig(context);
                }
            }
        }
    }


    // Configuration Getter & Setter

    /**
     * Get Application Configuration
     * <p>Instance will be created when Application created.</p>
     *
     * @return {@link ApplicationConfig#sConfig}
     */
    public static ApplicationConfig getConfig() {
        if (sConfig == null)
            throw new NullPointerException("You didn't call createApplicationConfig() in ApplicationContext");
        return sConfig;
    }

    public Context getContext() {
        return mContext;
    }

    public boolean isUserLogin() {
        return isUserLogin;
    }

    public void setUserLogin(boolean isUserLogin) {
        this.isUserLogin = isUserLogin;
        ThreadPoolUtil.getThreadPoolUtil().execute(() -> {
            mPreferences.edit().putBoolean(ConfigurationKeyEnum.IsUserLogin.toString(), isUserLogin).apply();
        });
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        mUsername = username;
        ThreadPoolUtil.getThreadPoolUtil().execute(() -> {
            mPreferences.edit().putString(ConfigurationKeyEnum.UserName.toString(), username).apply();
        });
    }

    public boolean isAutoBackup() {
        return isAutoBackup;
    }

    public void setAutoBackup(boolean autoBackup) {
        isAutoBackup = autoBackup;
        ThreadPoolUtil.getThreadPoolUtil().execute(() -> {
            mPreferences.edit().putBoolean(ConfigurationKeyEnum.IsAutoBackup.toString(), autoBackup).apply();
        });
    }

    public Float getMonthLimit() {
        return mMonthLimit;
    }

    public void setMonthLimit(Float monthLimit) {
        mMonthLimit = monthLimit;
        ThreadPoolUtil.getThreadPoolUtil().execute(() -> {
            mPreferences.edit().putFloat(ConfigurationKeyEnum.MonthLimit.toString(), monthLimit).apply();
        });
    }

    public boolean isNotify() {
        return isNotify;
    }

    public void setNotify(boolean notify) {
        isNotify = notify;
        ThreadPoolUtil.getThreadPoolUtil().execute(() -> {
            mPreferences.edit().putBoolean(ConfigurationKeyEnum.IsNotify.toString(), notify).apply();
        });
    }
}
