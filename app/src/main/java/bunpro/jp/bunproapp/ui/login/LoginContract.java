package bunpro.jp.bunproapp.ui.login;

import bunpro.jp.bunproapp.utils.SimpleCallbackListener;

public interface LoginContract {
    interface View {
        void loadingProgress(boolean stats);
        void showError(String txt);
        void gotoMain();
    }

    interface Presenter {
        void login(String email, String password, final SimpleCallbackListener callback);
        void configureSettings(final SimpleCallbackListener callback);
    }
}
