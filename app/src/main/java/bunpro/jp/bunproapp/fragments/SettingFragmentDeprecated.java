package bunpro.jp.bunproapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;
import com.wuadam.awesomewebview.AwesomeWebView;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import bunpro.jp.bunproapp.R;
import bunpro.jp.bunproapp.activities.LoginActivity;
import bunpro.jp.bunproapp.fragments.contract.SettingContract;
import bunpro.jp.bunproapp.fragments.contract.SettingController;
import bunpro.jp.bunproapp.utils.AppData;
import bunpro.jp.bunproapp.utils.Constants;
import bunpro.jp.bunproapp.utils.SettingEvent;

public class SettingFragmentDeprecated extends BaseFragment implements View.OnClickListener, SettingContract.View {

    RelativeLayout about;
    RelativeLayout bunnyMode;
    RelativeLayout contact;
    RelativeLayout furigana;
    RelativeLayout hideEnglish;
    RelativeLayout logout;
    RelativeLayout privacy;
    RelativeLayout terms;
    RelativeLayout community;

    SpinKitView progress;

    TextView tvFurigana, tvHideEnglish, tvBunnyMode;

    private Context mContext;
    private SettingContract.Controller mController;

    public SettingFragmentDeprecated() {

    }

    public static SettingFragmentDeprecated newInstance() {
        return new SettingFragmentDeprecated();
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

        mContext = getActivity();
        mController = new SettingController(mContext);

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

        community = view.findViewById(R.id.rlCommunity);
        community.setOnClickListener(this);

        tvFurigana = view.findViewById(R.id.tvFurigana);
        tvHideEnglish = view.findViewById(R.id.tvHideEnglish);
        tvBunnyMode = view.findViewById(R.id.tvBunnyMode);

        initialize();

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
            new AwesomeWebView.Builder(mContext).show(Constants.ABOUT_URL);
        }

        if (id == R.id.rlPrivacy) {
            new AwesomeWebView.Builder(mContext).show(Constants.PRIVACY_URL);
        }

        if (id == R.id.rlTerms) {
            new AwesomeWebView.Builder(mContext).show(Constants.TERMS_URL);
        }

        if (id == R.id.rlContact) {
            new AwesomeWebView.Builder(mContext).show(Constants.CONTACT_URL);

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

        if (id == R.id.rlCommunity) {
            showCommunity();
        }
    }

    private void initialize() {

        loadingProgress(false);

        int furigana = AppData.getInstance(mContext).getFurigana();
        switch (furigana) {
            case Constants.SETTING_FURIGANA_ALWAYS:
                tvFurigana.setText("Always");
                break;
            case Constants.SETTING_FURIGANA_NEVER:
                tvFurigana.setText("Never");
                break;
            case Constants.SETTING_FURIGANA_WANIKANI:
                tvFurigana.setText("WaniKani");
                break;
            default:
                tvFurigana.setText("Always");
                break;
        }

        int hideEnglish = AppData.getInstance(mContext).getHideEnglish();
        switch (hideEnglish) {
            case Constants.SETTING_HIDE_ENGLISH_YES:
                tvHideEnglish.setText("Yes");
                break;
            case Constants.SETTING_HIDE_ENGLISH_NO:
                tvHideEnglish.setText("No");
                break;
            default:
                tvHideEnglish.setText("No");
                break;
        }

        int bunnyMode = AppData.getInstance(mContext).getBunnyMode();
        switch (bunnyMode) {
            case Constants.SETTING_BUNNY_MODE_ON:
                tvBunnyMode.setText("On");
                break;
            case Constants.SETTING_BUNNY_MODE_OFF:
                tvBunnyMode.setText("Off");
                break;
            default:
                tvBunnyMode.setText("On");
                break;
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

                tvFurigana.setText("Always");
                AppData.getInstance(mContext).setFurigana(Constants.SETTING_FURIGANA_ALWAYS);
                setUserEdit();
                dialog.dismiss();
            }
        });

        RelativeLayout rlNever = view.findViewById(R.id.rlNever);
        rlNever.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvFurigana.setText("Never");
                AppData.getInstance(mContext).setFurigana(Constants.SETTING_FURIGANA_NEVER);
                setUserEdit();
                dialog.dismiss();
            }
        });

        RelativeLayout rlWanikani = view.findViewById(R.id.rlWanikani);
        rlWanikani.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvFurigana.setText("WaniKani");
                AppData.getInstance(mContext).setFurigana(Constants.SETTING_FURIGANA_WANIKANI);
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
                tvHideEnglish.setText("Yes");
                AppData.getInstance(mContext).setHideEnglish(Constants.SETTING_HIDE_ENGLISH_YES);
                setUserEdit();
                dialog.dismiss();
            }
        });

        RelativeLayout rlNo = view.findViewById(R.id.rlNo);
        rlNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvHideEnglish.setText("No");
                AppData.getInstance(mContext).setHideEnglish(Constants.SETTING_HIDE_ENGLISH_NO);
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

                tvBunnyMode.setText("On");
                AppData.getInstance(mContext).setBunnyMode(Constants.SETTING_BUNNY_MODE_ON);
                setUserEdit();
                dialog.dismiss();
            }
        });

        RelativeLayout rlOff = view.findViewById(R.id.rlOff);
        rlOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tvBunnyMode.setText("Off");
                AppData.getInstance(mContext).setBunnyMode(Constants.SETTING_BUNNY_MODE_OFF);
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

                mController.logout(SettingFragmentDeprecated.this);
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

        if (AppData.getInstance(mContext).getFurigana() == Constants.SETTING_FURIGANA_ALWAYS) {
            furigana = "Show";
        } else if (AppData.getInstance(mContext).getFurigana() == Constants.SETTING_FURIGANA_NEVER) {
            furigana = "Hide";
        } else {
            furigana = "Wanikani";
        }

        if (AppData.getInstance(mContext).getBunnyMode() == Constants.SETTING_BUNNY_MODE_ON) {
            bunny_mode = "On";
        } else {
            bunny_mode = "Off";
        }

        if (AppData.getInstance(mContext).getHideEnglish() == Constants.SETTING_HIDE_ENGLISH_NO) {
            hide_english = "No";
        } else {
            hide_english = "Yes";
        }

        mController.setEdit(hide_english, furigana, light_mode, bunny_mode, this);
    }

    @Override
    public void loadingProgress(boolean stats) {
        if (progress != null) {
            if (stats) {
                progress.setVisibility(View.VISIBLE);
            } else {
                progress.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void updateView() {
        Toast.makeText(mContext, "Updated user setting", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void showError(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void gotoLogin() {

        Intent intent = new Intent(mContext, LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    public void showCommunity() {

        new AwesomeWebView.Builder(mContext).showUrl(true).show(Constants.COMMUNITY_URL);

    }
}
