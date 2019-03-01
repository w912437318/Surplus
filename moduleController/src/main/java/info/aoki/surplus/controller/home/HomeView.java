package info.aoki.surplus.controller.home;

import info.aoki.surplus.resource.BaseView;

public interface HomeView extends BaseView {
    /**
     * <p>It's my be <strong>""</strong>, please check it</p>
     *
     * @return User input money
     */
    public String getUserInputMoney();

    /**
     * <p>It's my be <strong>""</strong>, please check it</p>
     *
     * @return User input memo
     */
    public String getUserInputMemo();

    /**
     * <p>Get user select record type</p>
     * <p><strong>Type:</strong> {@link HomeActivity}</p>
     *
     * @return Type
     */
    public int getUserSelectType();

    /**
     * Show consumed money
     *
     * @param consumedMoney {@link String}
     */
    public void setConsumedMoney(String consumedMoney);

    /**
     * Show current date
     *
     * @param currentDate {@link String}
     */
    public void setCurrentDate(String currentDate);
}
