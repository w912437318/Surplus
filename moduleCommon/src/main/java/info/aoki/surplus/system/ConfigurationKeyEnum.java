package info.aoki.surplus.system;

/**
 * <h3>ConfigurationKey</h3>
 * <p>Create time:12/4/18 7:58 PM</p>
 * <p>SharedPreferences of Config keys</p>
 *
 * @author wong
 * @version 1.0
 */
public enum ConfigurationKeyEnum {
    /**
     * SP 名称
     * <p>使用此名称获取配置文件 SP</p>
     */
    Config,
    /**
     * 用户登陆标记
     * <p>boolean 值，表示用户当前用户是否登陆</p>
     * <em>true : 表示登陆</em><br/>
     * <em>false : 表示未登陆</em>
     */
    IsUserLogin,
    /**
     * 用户名称
     */
    UserName,
    /**
     * 自动备份标记
     * <p>表示用户是否打开自动备份功能</p>
     * <em>true : 表示打开自动备份</em><br/>
     * <em>false : 表示未打开自动备份</em>
     */
    IsAutoBackup,
    /**
     * 每月生活费
     * <p>用户设置的每月生活费，也就是每月的消费限额</p>
     */
    MonthLimit,
    /**
     * 是否通知用户记账标记
     * <em>true : 表示发送通知</em><br/>
     * <em>false : 表示不发送通知</em>
     */
    IsNotify,
    /**
     * 用户是否是第一次运行软件
     * <em>true : 表示用户是第一次运行</em><br/>
     * <em>false : 表示不是第一次运行</em>
     */
    IsFirstRun
}
