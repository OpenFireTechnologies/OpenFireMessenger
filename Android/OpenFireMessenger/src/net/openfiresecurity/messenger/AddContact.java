package net.openfiresecurity.messenger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddContact extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prompt_add_contact);

        final EditText etUser = (EditText) findViewById(R.id.etPromptName);
        final EditText etEmail = (EditText) findViewById(R.id.etPromptEmail);
        final Button ok = (Button) findViewById(R.id.bPromptOk);
        ok.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (etUser.getText().toString().trim().isEmpty()
                        && etEmail.getText().toString().trim().isEmpty()) {
                    Toast.makeText(AddContact.this,
                            "Please fill in all fields!", Toast.LENGTH_LONG)
                            .show();
                } else {
                    Bundle data = new Bundle();
                    data.putString("user", etUser.getText().toString().trim());
                    data.putString("email", etEmail.getText().toString().trim());
                    Intent intent = new Intent();
                    intent.putExtras(data);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }

        });
    }

}
