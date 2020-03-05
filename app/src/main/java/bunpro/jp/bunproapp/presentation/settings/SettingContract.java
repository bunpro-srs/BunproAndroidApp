package bunpro.jp.bunproapp.presentation.settings;

import android.content.Context;

public interface SettingContract {
    interface View {
        Context getContext();
        void setLoadingProgress(boolean loading);
        void goToLogin();
        void showError(String msg);
        void notifyUpdate();
    }

    interface Presenter {
        void logout();
        void submitSettings(String hideEnglish, String furigana, String lightMode, String bunnyMode);
    }
}
