package com.jogern.jui.ui;

import android.content.Intent;
import android.os.Bundle;

/**
 * Create on 2019-08-23.
 *
 * @author zujianliang
 */
public class LauncherActivity extends PermissionActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkPermission();
    }

    @Override
    protected void startLauncherActivity() {
        startActivity(new Intent(this, MainActivity.class));
    }
}
