package bunpro.jp.bunprosrs.fragments.contract;

public interface SettingContract {

    interface View {

        void loadingProgress(boolean stats);
        void showError(String msg);
        void gotoLogin();
    }

    interface Controller {
        void logout(SettingContract.View v);
    }
}
