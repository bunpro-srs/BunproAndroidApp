package bunpro.jp.bunproapp.activities.contract;

public interface LoginContract {
    interface View {
        void loadingProgress(boolean stats);
        void showError(String txt);
        void gotoMain();
    }

    interface Controller {
        void login(String email, String password, final LoginController.SimpleCallbackListener callback);
        void configureSettings(final LoginController.SimpleCallbackListener callback);
    }
}
