package edu.wayne.service.home;

import info.aoki.surplus.resource.BaseView;

public interface HomeView extends BaseView {

    /**
     * 获取用户输入的消费金额
     *
     * @return String(未校验)
     */
    String getUserInputMoney();

    /**
     * 获取用户输入的消费备注
     *
     * @return String(未校验)
     */
    String getUserInputMemo();

    /**
     * 获取用户选择的消费类型
     *
     * @return int
     */
    int getSelectedConsumedTypeId();

    /**
     * 设置当月消费总金额
     *
     * @param consumedMoney 消费金额
     */
    void setConsumedMoney(String consumedMoney);

    /**
     * 设置当前日期
     *
     * @param currentDate 当前日期
     */
    void setCurrentDate(String currentDate);

    /**
     * 设置用户输入金额错误时的提示
     *
     * @param errorHint 错误提示
     */
    void setInputMoneyErrorHint(String errorHint);

    /**
     * 重置用户输入的信息
     * <p>在成功保存用户输入的信息后清空输入框、关闭输入弹窗等</p>
     */
    void resetInputInfo();
}
