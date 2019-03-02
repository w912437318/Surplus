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
    private boolean isUserLogin;
    private String mUsername;
    private boolean isAutoBackup;
    private float mMonthLimit;
    private boolean isNotify;
    @SuppressLint("StaticFieldLeak")
    private static ApplicationConfig sConfig;

    private Context mContext;

    private SharedPreferences mPreferences;

    private ApplicationConfig(Context context) {
        this.mContext = context;
        this.mPreferences = mContext.getSharedPreferences(
                ConfigurationKeyEnum.Config.toString(),
                Context.MODE_PRIVATE);
        this.loadConfiguration();
    }

    private void loadConfiguration() {
        ThreadPoolUtil.getThreadPoolUtil().execute(() -> {
            ApplicationConfig.this.isUserLogin = mPreferences.getBoolean(ConfigurationKeyEnum.IsUserLogin.toString(), false);
            mUsername = mPreferences.getString(ConfigurationKeyEnum.UserName.toString(), "");
            if (mUsername.equals("") && !isUserLogin) {
                setUsername(UuidUtil.getRandomUUID());
            }
            ApplicationConfig.this.mMonthLimit = mPreferences.getFloat(ConfigurationKeyEnum.MonthLimit.toString(), 1000f);
            ApplicationConfig.this.isNotify = mPreferences.getBoolean(ConfigurationKeyEnum.IsNotify.toString(), false);
        });
    }

    public static void createApplicationConfig(Context context) {
        if (sConfig == null) {
            synchronized (ApplicationConfig.class) {
                if (sConfig == null) {
                    sConfig = new ApplicationConfig(context);
                }
            }
        }
    }


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
