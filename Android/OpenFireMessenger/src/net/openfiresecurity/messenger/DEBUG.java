/*
 * Copyright (c) 2013. Alexander Martinz.
 */

package net.openfiresecurity.messenger;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Alex on 20.05.13.
 */
public class DEBUG extends Activity implements View.OnClickListener {

    Button bDebugNumber;
    TextView tvDebug;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.debug);
        bDebugNumber = (Button) findViewById(R.id.bDebugNumber);
        bDebugNumber.setOnClickListener(this);

        tvDebug = (TextView) findViewById(R.id.tvDebug);
    }

    @Override
    public void onClick(@NotNull View view) {
        switch (view.getId()) {
            case R.id.bDebugNumber:
                TelephonyManager mTelephonyMgr;
                mTelephonyMgr = (TelephonyManager)
                        getSystemService(Context.TELEPHONY_SERVICE);

                tvDebug.setText(mTelephonyMgr.getSimSerialNumber());
                break;
        }
    }


}
