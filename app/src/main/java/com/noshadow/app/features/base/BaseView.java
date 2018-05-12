package com.noshadow.app.features.base;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.StringRes;

/**
 * Created by Liga on 27/06/2017.
 */

public interface BaseView {
	Activity asActivity();

	String getString(@StringRes int resourceId);

    void showLoading(String message);

    void showLoading();

    void hideLoading();

    void showMessage(String message);

    void showMessage(String tittle, String message, String buttonContent);

    void showMessage(String tittle, String message, String buttonOkContent, String buttonCancelContent);

    void startActivity(Class<?> activity);

    void startActivityForResult(Class<?> activity);

	void switchToPreviousActivity();

	void showToast(String message, boolean isLongToast);

}
