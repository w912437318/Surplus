package info.aoki.surplus.controller.home;

import info.aoki.surplus.resource.BaseView;

public interface HomeView extends BaseView {

    String getUserInputMoney();

    String getUserInputMemo();

    int getUserSelectType();

    void setConsumedMoney(String consumedMoney);

    void setCurrentDate(String currentDate);
}
