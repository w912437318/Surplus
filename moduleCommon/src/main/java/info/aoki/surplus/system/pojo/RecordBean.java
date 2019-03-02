package info.aoki.surplus.system.pojo;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;

import java.util.Date;

/**
 * <h3>Record Bean</h3>
 * <p>Create time:12/3/18 4:31 PM</p>
 *
 * @author wong
 * @version 1.0
 */
@Entity(nameInDb = "record_tb",
        createInDb = true)
public class RecordBean {
    @Id(autoincrement = true)
    private Long r_id;// Record ID

    @Property(nameInDb = "r_money")
    @NotNull
    private float r_money;

    @Property(nameInDb = "r_memo")
    private String r_memo;

    @Property(nameInDb = "r_time")
    @NotNull
    private long r_time;

    @Property(nameInDb = "r_type")
    @NotNull
    private String r_type;

    @Property(nameInDb = "r_back_flag")
    private boolean isBackup;

    public RecordBean(float r_money, String r_type) {
        this.r_money = r_money;
        this.r_type = r_type;
        this.r_time = new Date().getTime();
        this.isBackup = false;
    }

    @Generated(hash = 304308005)
    public RecordBean(Long r_id, float r_money, String r_memo, long r_time,
            @NotNull String r_type, boolean isBackup) {
        this.r_id = r_id;
        this.r_money = r_money;
        this.r_memo = r_memo;
        this.r_time = r_time;
        this.r_type = r_type;
        this.isBackup = isBackup;
    }

    @Generated(hash = 96196931)
    public RecordBean() {
    }

    public Long getR_id() {
        return this.r_id;
    }

    public void setR_id(Long r_id) {
        this.r_id = r_id;
    }

    public float getR_money() {
        return this.r_money;
    }

    public void setR_money(float r_money) {
        this.r_money = r_money;
    }

    public String getR_memo() {
        return this.r_memo;
    }

    public void setR_memo(String r_memo) {
        this.r_memo = r_memo;
    }

    public long getR_time() {
        return this.r_time;
    }

    public void setR_time(long r_time) {
        this.r_time = r_time;
    }

    public String getR_type() {
        return this.r_type;
    }

    public void setR_type(String r_type) {
        this.r_type = r_type;
    }

    public boolean getIsBackup() {
        return this.isBackup;
    }

    public void setIsBackup(boolean isBackup) {
        this.isBackup = isBackup;
    }

}
