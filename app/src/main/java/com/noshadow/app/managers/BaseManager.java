package com.noshadow.app.managers;

import android.content.Context;

/**
 * Created by Liga on 27/06/2017.
 */

public class BaseManager {
    protected Context _context;
    protected String _appName;

    public String getString(int resource) {
        return _context.getResources().getString(resource);
    }
}
