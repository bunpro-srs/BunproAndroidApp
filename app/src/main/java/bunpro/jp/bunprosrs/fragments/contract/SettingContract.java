package bunpro.jp.bunprosrs.fragments.contract;

public interface SettingContract {

    interface View {

        void loadingProgress(boolean stats);
        void updateView();
        void showError(String msg);
        void gotoLogin();
    }

    interface Controller {
        void logout(SettingContract.View v);
        void setEdit(String hideEnglish, String furigana, String lightMode, String bunnyMode, SettingContract.View v);

    }
}
