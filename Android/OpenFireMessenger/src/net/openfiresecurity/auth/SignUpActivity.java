/*
 * Copyright (c) 2013. Alexander Martinz @ OpenFire Security
 */

package net.openfiresecurity.auth;

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import net.openfiresecurity.helper.ResourceManager;
import net.openfiresecurity.messenger.R;

import static net.openfiresecurity.auth.AccountGeneral.sServerAuthenticate;
import static net.openfiresecurity.auth.AuthenticatorActivity.ARG_ACCOUNT_TYPE;
import static net.openfiresecurity.auth.AuthenticatorActivity.ARG_IS_ADDING_NEW_ACCOUNT;
import static net.openfiresecurity.auth.AuthenticatorActivity.KEY_ERROR_MESSAGE;
import static net.openfiresecurity.auth.AuthenticatorActivity.PARAM_USER_PASS;

public class SignUpActivity extends Activity {

    private String mAccountType;

    // Elements
    private EditText etPass, etUser, etEmail;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAccountType = getIntent().getStringExtra(ARG_ACCOUNT_TYPE);

        setContentView(R.layout.act_register);

        dialog = new ProgressDialog(this);

        TextView tvMember = (TextView) findViewById(R.id.alreadyMember);
        Button submit = (Button) findViewById(R.id.submit);

        etUser = (EditText) findViewById(R.id.name);
        etEmail = (EditText) findViewById(R.id.accountName);
        etPass = (EditText) findViewById(R.id.accountPassword);

        submit.setTypeface(ResourceManager.L_STEINER);
        tvMember.setTypeface(ResourceManager.L_STEINER);
        etUser.setTypeface(ResourceManager.L_STEINER);
        etEmail.setTypeface(ResourceManager.L_STEINER);
        etPass.setTypeface(ResourceManager.L_STEINER);

        tvMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(etUser.getText().toString().trim().isEmpty())
                        && !(etEmail.getText().toString().trim().isEmpty())
                        && !(etPass.getText().toString().trim().isEmpty())) {
                    dialog.setTitle("Please wait...");
                    dialog.setMessage("Signing you up, faster than the light!");
                    dialog.setCancelable(false);
                    dialog.show();
                    createAccount();
                } else {
                    Toast.makeText(SignUpActivity.this,
                            "Please fill in all fields!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });
    }

    private void createAccount() {

        // Validation!
        new AsyncTask<String, Void, Intent>() {

            final String name = etUser.getText().toString().trim();
            final String accountName = etEmail.getText().toString().trim();
            final String accountPassword = etPass.getText().toString().trim();

            @Override
            protected Intent doInBackground(String... params) {

                String authtoken;
                Bundle data = new Bundle();
                try {
                    authtoken = sServerAuthenticate.userSignUp(name,
                            accountName, accountPassword);
                    data.putString(AccountManager.KEY_ACCOUNT_NAME, accountName);
                    data.putString(AccountManager.KEY_ACCOUNT_TYPE,
                            mAccountType);
                    data.putString(AccountManager.KEY_AUTHTOKEN, authtoken);
                    data.putString(PARAM_USER_PASS, accountPassword);
                    data.putString("email", accountName);
                    data.putBoolean(ARG_IS_ADDING_NEW_ACCOUNT, true);
                } catch (Exception e) {
                    data.putString(KEY_ERROR_MESSAGE,
                            "Error: " + e.getMessage());
                }

                final Intent res = new Intent();
                res.putExtras(data);
                return res;
            }

            @Override
            protected void onPostExecute(Intent intent) {
                if (intent.hasExtra(KEY_ERROR_MESSAGE)) {
                    Toast.makeText(getBaseContext(),
                            intent.getStringExtra(KEY_ERROR_MESSAGE),
                            Toast.LENGTH_SHORT).show();
                } else {
                    setResult(RESULT_OK, intent);
                    finish();
                }
                dialog.dismiss();
            }
        }.execute();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }
}
