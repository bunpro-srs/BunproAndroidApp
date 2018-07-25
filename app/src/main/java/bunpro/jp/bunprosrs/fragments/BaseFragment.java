package bunpro.jp.bunprosrs.fragments;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import bunpro.jp.bunprosrs.R;
import bunpro.jp.bunprosrs.activities.MainActivity;

public class BaseFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void replaceFragment(Fragment fragment, boolean backToStack) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (backToStack) {
            fragmentTransaction.addToBackStack(fragment.getClass().getName());
        }

        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }

    protected void addFragment(Fragment fragment, boolean backToStack) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (backToStack) {
            fragmentTransaction.addToBackStack(fragment.getClass().getName());
        }

        fragmentTransaction.add(R.id.container, fragment);
        fragmentTransaction.commit();
    }

    protected void popFragment() {
        ((MainActivity)getActivity()).popFragment();
//        if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0){
//            boolean done = getActivity().getSupportFragmentManager().popBackStackImmediate();
//        }
    }


    protected void clipTextToBoard(String text) {

        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Bunpro", text);
        clipboard.setPrimaryClip(clip);
    }
}
