package bunpro.jp.bunproapp.activities.contract;

public interface LoginContract {
    interface View {
        void loadingProgress(boolean stats);
        void showError(String txt);
        void gotoMain();
    }

    interface Controller {
        void login(LoginContract.View v, String email, String password);
    }
}
