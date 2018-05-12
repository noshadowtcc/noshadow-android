package com.noshadow.app.features.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.noshadow.app.R;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;


import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by Liga on 27/06/2017.
 */

public class BaseActivity extends AppCompatActivity implements BaseView,
        SearchView.OnQueryTextListener {

    Boolean active = false;
    MaterialDialog progress;

    TextView _txtBgItemsCount;
    FrameLayout _frmBgItemsCount;

    ArrayList<Fragment> fragments = new ArrayList<>();

    private boolean search = false;

    public Context getContext() {
        return this;
    }

    public void setSearchBar() {
        search = true;
    }

    private Window lollipopWindow;

    @Override
    public void showLoading(String message) {
        if (active) {
            if (progress == null) {
                progress = new MaterialDialog.Builder(this).title("Aguarde")
                        .content(message).widgetColor(getResources()
                                .getColor(R.color.colorPrimaryDark))
                        .cancelable(false).progress(true, 0).show();
            } else {
                progress.setContent(message);
                progress.show();
            }
        }
    }

    public void setActive(){
        this.active = true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            lollipopWindow = getWindow();
            lollipopWindow.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            lollipopWindow.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!search)
            return super.onCreateOptionsMenu(menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    

    @Override
    public void showLoading() {
        if (active)
            showLoading("Por favor, aguarde...");
    }

    @Override
    public void hideLoading() {
        if (active) {
            try {
                runOnUiThread(new Runnable() {
                    public void run() {
                        if (progress != null)
                            progress.dismiss();

                    }
                });
            } catch (Exception e) {
                //TODO: arrumar esse bug maldito :(
            }
        }
    }
    
    public static boolean isPreLollipop()
    {
	    return android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP;
    }

    @Override
    public void showMessage(final String tittle, final String message, final String buttonContent){
        if (active)
            try {
                runOnUiThread(new Runnable() {
                    public void run() {
                        new MaterialDialog.Builder(getActivity()).title(tittle).content(message)
                                .positiveText(buttonContent).autoDismiss(true)
                                .positiveColor(getResources().getColor(R.color.colorPrimaryDark))
                                .show();
                    }
                });
            } catch (Exception e) {
                //TODO: arrumar esse bug maldito :(
            }
    }

    @Override
    public void showMessage(final String tittle, final String message, final String buttonOkContent,
                            final String buttonCancelContent) {
        if (active)
            try {
                runOnUiThread(new Runnable() {
                    public void run() {
                        new MaterialDialog.Builder(getActivity()).title(tittle).content(message)
                                .positiveText(buttonOkContent).negativeText(buttonCancelContent)
                                .autoDismiss(true).positiveColor(getResources()
                                .getColor(R.color.colorPrimaryDark)).show();
                    }
                });
            } catch (Exception e) {
                //TODO: arrumar esse bug maldito :(
            }
    }

    @Override
        public void showMessage(final String message) {

            if (active)
                try {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            new MaterialDialog.Builder(getActivity()).title("Atenção")
                                    .content(message).positiveText("Ok").positiveColor(getResources()
                                    .getColor(R.color.colorPrimaryDark)).autoDismiss(true).show();
                        }
                    });
                } catch (Exception e) {
                    //TODO: arrumar esse bug maldito :(
                }
        }

    public Context getActivity() {
        return this;
    }

    public void startActivity(Class<?> activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }

    public void startActivity(Class<?> activity, int animationStart, int animationEnd) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
        overridePendingTransition(animationStart,animationEnd);
    }

    public void startActivityForResult(Class<?> activity) {
        Intent intent = new Intent(this, activity);
        startActivityForResult(intent, 1);
    }

    public ArrayList<Fragment> getFragments() {
        return fragments;
    }

    public void setFragment(int contentId, int fragmentIndex) {
        if (active) {
            if (getFragments().size() > 0) {
                getSupportFragmentManager().beginTransaction().replace(contentId, getFragments()
                        .get(fragmentIndex)).commitAllowingStateLoss();

            }
        }
    }

    @NonNull
    public InputFilter[] getInputFilterToPassword() {
        InputFilter filter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    if (Character.isWhitespace(source.charAt(i))) {
                        return "";
                    }
                }
                return null;
            }
        };

        return new InputFilter[]{filter};
    }

    public Boolean isActive() {
        return active;
    }

    @Override
    public void onResume() {
        super.onResume();
        active = true;
    }

    @Override
    public void onStart() {
        super.onStart();
        active = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        active = false;
    }

    @Override
    public void onPause() {
        super.onPause();
        active = false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    protected void setStatusBarColor(int color) {
        if (lollipopWindow != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            lollipopWindow.setStatusBarColor(getResources().getColor(color));
    }

	//region // switchActivity(): Starts a new Activity of the parameter class.


	/**
	 * Finishes current Activity, going back tom the previous one from the Back Stack.
	 */
	@Override
	public void switchToPreviousActivity()
	{
		finish();
	}

	public void showToast(String message, boolean isLongToast)
	{
		Toast.makeText(this, message, isLongToast ? Toast.LENGTH_LONG :
                Toast.LENGTH_SHORT).show();
	}

	public Activity asActivity()
	{
		return this;
	}

	//endregion
}
