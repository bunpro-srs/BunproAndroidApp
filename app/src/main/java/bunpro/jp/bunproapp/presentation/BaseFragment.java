package bunpro.jp.bunproapp.presentation;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import bunpro.jp.bunproapp.R;
import bunpro.jp.bunproapp.presentation.home.HomeActivity;

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

        fragmentTransaction.replace(R.id.main_container, fragment);
        fragmentTransaction.commit();
    }

    protected void addFragment(Fragment fragment, boolean backToStack) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (backToStack) {
            fragmentTransaction.addToBackStack(fragment.getClass().getName());
        }

        fragmentTransaction.add(R.id.main_container, fragment);
        fragmentTransaction.commit();
    }

    protected void popFragment() {
        ((HomeActivity)getActivity()).popFragment();
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
