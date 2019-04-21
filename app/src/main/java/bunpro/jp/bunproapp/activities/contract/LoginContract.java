package bunpro.jp.bunproapp.activities.contract;

import bunpro.jp.bunproapp.utils.SimpleCallbackListener;

public interface LoginContract {
    interface View {
        void loadingProgress(boolean stats);
        void showError(String txt);
        void gotoMain();
    }

    interface Controller {
        void login(String email, String password, final SimpleCallbackListener callback);
        void configureSettings(final SimpleCallbackListener callback);
    }
}
