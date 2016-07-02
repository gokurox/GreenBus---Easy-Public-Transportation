package com.example.gursimransingh.greenbus_evs_iiitd.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gursimransingh.greenbus_evs_iiitd.R;
import com.example.gursimransingh.greenbus_evs_iiitd.sqlite.DatabaseHelper;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LoginActivity extends AppCompatActivity {

    @InjectView (R.id.input_email) EditText _emailText;
    @InjectView (R.id.input_password) EditText _passwordText;
    @InjectView (R.id.btn_login) Button _loginButton;
    @InjectView (R.id.link_signup) TextView _signupLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.inject (this);

        // Set LoginButton OnClickListener
        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Do Login
                login();
            }
        });

        // Set SignupLink OnClickListener
        _signupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent switchToSignUpActivity = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity (switchToSignUpActivity);
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack (true);
    }

    private void login() {

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled (false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this, R.style.AppTheme_Dark_Dialog);

        progressDialog.setIndeterminate (true);
        progressDialog.setMessage ("Authenticating...");
        progressDialog.show();

        final String email = _emailText.getText().toString();
        final String password = _passwordText.getText().toString();

        DatabaseHelper db = DatabaseHelper.getInstance (this);

        if (db.ULI_login(this, email, password))
        {
            // Authenticated
            _loginButton.setEnabled(false);
            Intent switchToNavigatorActivity = new Intent (LoginActivity.this, NavigatorActivity.class);
            new android.os.Handler().postDelayed (
                new Runnable() {
                    public void run() {
                        progressDialog.dismiss();
                    }
                }, 1000);
            startActivity (switchToNavigatorActivity);
        }
        else
        {
            // Failed
            new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        progressDialog.dismiss();
                    }
                }, 1000);
            onLoginFailed();
        }
    }

    private void onLoginFailed() {
        Toast.makeText (this, "Login failed !!", Toast.LENGTH_LONG).show();
        _loginButton.setEnabled (true);
    }

    private boolean validate () {
        boolean valid = true;

        // Get textData
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        // Email Validity Check
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            _emailText.setError ("Enter a valid email address");
            valid = false;
        }
        else
            _emailText.setError (null);

        // Password Validity Check
        if (password.isEmpty() || password.length() < 4 || password.length() > 15)
        {
            _passwordText.setError ("Between 4 and 15 characters");
            valid = false;
        } else
            _passwordText.setError (null);

        return valid;
    }
}
