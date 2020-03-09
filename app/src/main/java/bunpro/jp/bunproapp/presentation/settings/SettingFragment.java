package bunpro.jp.bunproapp.presentation.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import bunpro.jp.bunproapp.R;
import bunpro.jp.bunproapp.presentation.BunproWebView;
import bunpro.jp.bunproapp.presentation.home.HomeActivity;
import bunpro.jp.bunproapp.presentation.login.LoginActivity;
import bunpro.jp.bunproapp.presentation.BaseFragment;
import bunpro.jp.bunproapp.utils.config.AppData;
import bunpro.jp.bunproapp.utils.config.Constants;
import bunpro.jp.bunproapp.utils.SettingEvent;

public class SettingFragment extends BaseFragment implements View.OnClickListener, SettingContract.View {
    private SettingContract.Presenter settingPresenter;
    private Context context;

    RelativeLayout about;
    RelativeLayout bunnyMode;
    RelativeLayout contact;
    RelativeLayout furigana;
    RelativeLayout hideEnglish;
    RelativeLayout logout;
    RelativeLayout refreshDatabase;
    RelativeLayout privacy;
    RelativeLayout terms;
    RelativeLayout community;
    SpinKitView progress;
    TextView tvFurigana, tvHideEnglish, tvBunnyMode;

    public SettingFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.fragment_setting, container, false);

        context = getActivity();
        settingPresenter = new SettingPresenter(this);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progress = view.findViewById(R.id.spin_kit);

        about = view.findViewById(R.id.rlAbout);
        about.setOnClickListener(this);

        privacy = view.findViewById(R.id.rlPrivacy);
        privacy.setOnClickListener(this);

        terms = view.findViewById(R.id.rlTerms);
        terms.setOnClickListener(this);

        contact = view.findViewById(R.id.rlContact);
        contact.setOnClickListener(this);

        furigana = view.findViewById(R.id.rlFurigana);
        furigana.setOnClickListener(this);

        hideEnglish = view.findViewById(R.id.rlHideEnglish);
        hideEnglish.setOnClickListener(this);

        bunnyMode = view.findViewById(R.id.rlBunnyMode);
        bunnyMode.setOnClickListener(this);

        logout = view.findViewById(R.id.rlLogout);
        logout.setOnClickListener(this);

        refreshDatabase = view.findViewById(R.id.rlRefreshDatabase);
        refreshDatabase.setOnClickListener(this);

        community = view.findViewById(R.id.rlCommunity);
        community.setOnClickListener(this);

        tvFurigana = view.findViewById(R.id.tvFurigana);
        tvHideEnglish = view.findViewById(R.id.tvHideEnglish);
        tvBunnyMode = view.findViewById(R.id.tvBunnyMode);

        int furigana = AppData.getInstance(context).getFurigana();
        switch (furigana) {
            case Constants.SETTING_FURIGANA_ALWAYS:
                tvFurigana.setText(R.string.always);
                break;
            case Constants.SETTING_FURIGANA_NEVER:
                tvFurigana.setText(R.string.never);
                break;
            case Constants.SETTING_FURIGANA_WANIKANI:
                tvFurigana.setText(R.string.wanikani);
                break;
            default:
                tvFurigana.setText(R.string.always);
                break;
        }

        int hideEnglish = AppData.getInstance(context).getHideEnglish();
        switch (hideEnglish) {
            case Constants.SETTING_HIDE_ENGLISH_YES:
                tvHideEnglish.setText(R.string.yes);
                break;
            case Constants.SETTING_HIDE_ENGLISH_NO:
                tvHideEnglish.setText(R.string.no);
                break;
            default:
                tvHideEnglish.setText(R.string.no);
                break;
        }

        int bunnyMode = AppData.getInstance(context).getBunnyMode();
        switch (bunnyMode) {
            case Constants.SETTING_BUNNY_MODE_ON:
                tvBunnyMode.setText(R.string.on);
                break;
            case Constants.SETTING_BUNNY_MODE_OFF:
                tvBunnyMode.setText(R.string.off);
                break;
            default:
                tvBunnyMode.setText(R.string.on);
                break;
        }

        setLoadingProgress(false);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();
        if (id == R.id.rlAbout) {
            BunproWebView.instantiate(context, Constants.ABOUT_URL);
        }

        if (id == R.id.rlPrivacy) {
            BunproWebView.instantiate(context, Constants.PRIVACY_URL);
        }

        if (id == R.id.rlTerms) {
            BunproWebView.instantiate(context, Constants.TERMS_URL);
        }

        if (id == R.id.rlContact) {
            BunproWebView.instantiate(context, Constants.CONTACT_URL);
        }

        if (id == R.id.rlFurigana) {
            showFurigana();
        }

        if (id == R.id.rlHideEnglish) {
            showHideEnglish();
        }

        if (id == R.id.rlBunnyMode) {
            showBunnyMode();
        }

        if (id == R.id.rlLogout) {
            showLogout();
        }

        if (id == R.id.rlRefreshDatabase) {
            beginDatabaseRefresh();
        }

        if (id == R.id.rlCommunity) {
            BunproWebView.instantiate(context, Constants.COMMUNITY_URL);
        }
    }

    public void setLoadingProgress(boolean loading) {
        if (progress != null) {
            if (loading) {
                progress.setVisibility(View.VISIBLE);
            } else {
                progress.setVisibility(View.GONE);
            }
        }
    }

    private void showFurigana() {

        View view = getLayoutInflater().inflate(R.layout.layout_furigana, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(getActivity());
        dialog.setContentView(view);
        dialog.show();

        RelativeLayout rlAlways = view.findViewById(R.id.rlAlways);
        rlAlways.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tvFurigana.setText(R.string.always);
                AppData.getInstance(context).setFurigana(Constants.SETTING_FURIGANA_ALWAYS);
                setUserEdit();
                dialog.dismiss();
            }
        });

        RelativeLayout rlNever = view.findViewById(R.id.rlNever);
        rlNever.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvFurigana.setText(R.string.never);
                AppData.getInstance(context).setFurigana(Constants.SETTING_FURIGANA_NEVER);
                setUserEdit();
                dialog.dismiss();
            }
        });

        RelativeLayout rlWanikani = view.findViewById(R.id.rlWanikani);
        rlWanikani.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvFurigana.setText(R.string.wanikani);
                AppData.getInstance(context).setFurigana(Constants.SETTING_FURIGANA_WANIKANI);
                setUserEdit();
                dialog.dismiss();
            }
        });

        RelativeLayout rlCancel = view.findViewById(R.id.rlCancel);
        rlCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private void showHideEnglish() {

        View view = getLayoutInflater().inflate(R.layout.layout_hide_english, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(getActivity());
        dialog.setContentView(view);

        dialog.show();

        RelativeLayout rlYes = view.findViewById(R.id.rlYes);
        rlYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvHideEnglish.setText(R.string.yes);
                AppData.getInstance(context).setHideEnglish(Constants.SETTING_HIDE_ENGLISH_YES);
                setUserEdit();
                dialog.dismiss();
            }
        });

        RelativeLayout rlNo = view.findViewById(R.id.rlNo);
        rlNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvHideEnglish.setText(R.string.no);
                AppData.getInstance(context).setHideEnglish(Constants.SETTING_HIDE_ENGLISH_NO);
                setUserEdit();
                dialog.dismiss();
            }
        });

        RelativeLayout rlCancel = view.findViewById(R.id.rlCancel);
        rlCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

    private void showBunnyMode() {
        View view = getLayoutInflater().inflate(R.layout.layout_bunny_mode, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(getActivity());

        dialog.setContentView(view);

        dialog.show();

        RelativeLayout rlOn = view.findViewById(R.id.rlOn);
        rlOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tvBunnyMode.setText(R.string.on);
                AppData.getInstance(context).setBunnyMode(Constants.SETTING_BUNNY_MODE_ON);
                setUserEdit();
                dialog.dismiss();
            }
        });

        RelativeLayout rlOff = view.findViewById(R.id.rlOff);
        rlOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tvBunnyMode.setText(R.string.off);
                AppData.getInstance(context).setBunnyMode(Constants.SETTING_BUNNY_MODE_OFF);
                setUserEdit();
                dialog.dismiss();
            }
        });

        RelativeLayout rlCancel = view.findViewById(R.id.rlCancel);
        rlCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });
    }

    private void showLogout() {
        View view = getLayoutInflater().inflate(R.layout.layout_logout, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(getActivity());

        dialog.setContentView(view);
        dialog.show();

        RelativeLayout rlLogout = view.findViewById(R.id.rlOut);
        rlLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                settingPresenter.logout();
                dialog.dismiss();
            }
        });

        RelativeLayout rlCancel = view.findViewById(R.id.rlCancel);
        rlCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SettingEvent event) {

    }

    private void setUserEdit() {

        String furigana = "";
        String bunny_mode = "";
        String hide_english = "";
        String light_mode = "Off";

        if (AppData.getInstance(context).getFurigana() == Constants.SETTING_FURIGANA_ALWAYS) {
            furigana = "Show";
        } else if (AppData.getInstance(context).getFurigana() == Constants.SETTING_FURIGANA_NEVER) {
            furigana = "Hide";
        } else {
            furigana = "Wanikani";
        }

        if (AppData.getInstance(context).getBunnyMode() == Constants.SETTING_BUNNY_MODE_ON) {
            bunny_mode = "On";
        } else {
            bunny_mode = "Off";
        }

        if (AppData.getInstance(context).getHideEnglish() == Constants.SETTING_HIDE_ENGLISH_NO) {
            hide_english = "No";
        } else {
            hide_english = "Yes";
        }

        settingPresenter.submitSettings(hide_english, furigana, light_mode, bunny_mode);
    }

    // TODO: delete this method once this is no longer useful
    private void beginDatabaseRefresh() {
        Toast.makeText(context, R.string.refreshing_database, Toast.LENGTH_SHORT).show();
        if (context instanceof HomeActivity) {
            if (((HomeActivity)context).homePresenter != null) {
                ((HomeActivity)context).homePresenter.fetchData();
                return;
            }
        }
        Toast.makeText(context, "Unable to refresh database", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void notifyUpdate() {
        Toast.makeText(context, R.string.settings_update, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showError(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void goToLogin() {
        Intent intent = new Intent(context, LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}
