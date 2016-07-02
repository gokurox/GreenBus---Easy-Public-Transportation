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
import com.example.gursimransingh.greenbus_evs_iiitd.sqlite.dataholder.UserInfo;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SignUpActivity extends AppCompatActivity {

    @InjectView (R.id.input_name) EditText _nameText;
    @InjectView (R.id.input_email) EditText _emailText;
    @InjectView (R.id.input_password) EditText _passwordText;
    @InjectView (R.id.btn_signup) Button _signupButton;
    @InjectView (R.id.link_login) TextView _loginLink;
    @InjectView (R.id.input_phone) TextView _mobileText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        ButterKnife.inject(this);

        // Set SignupButton OnClickListener
        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        // Set LoginLink OncClickListener
        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent switchToLoginActivity = new Intent (SignUpActivity.this, LoginActivity.class);
                startActivity (switchToLoginActivity);
            }
        });
    }

    public void signup() {

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled (false);

        final ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this, R.style.AppTheme_Dark_Dialog);

        progressDialog.setIndeterminate (true);
        progressDialog.setMessage ("Creating Account...");
        progressDialog.show();

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String mobile = _mobileText.getText().toString();
        String password = _passwordText.getText().toString();

        DatabaseHelper db = DatabaseHelper.getInstance (this);

        if (db.ULI_getData (this, email) != null)
        {
            _emailText.setError("Duplicate Email");

            new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        progressDialog.dismiss();
                    }
                }, 1000);

            onSignupFailed();
        }
        else
        {
            UserInfo userInfo = new UserInfo (email, mobile, name, password, "ONLINE");
            db.ULI_insertData (userInfo);

            new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        progressDialog.dismiss();
                    }
                }, 1000);

            onSignupSuccess();
        }
    }

    public void onSignupSuccess() {
        _signupButton.setEnabled(false);
        Intent switchToNavigatorActivity = new Intent (SignUpActivity.this, NavigatorActivity.class);
        startActivity (switchToNavigatorActivity);
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "SignUp failed !!", Toast.LENGTH_LONG).show();
        _signupButton.setEnabled (true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String mobile = _mobileText.getText().toString();
        String password = _passwordText.getText().toString();

        // Name Validity Check
        if (name.isEmpty() || name.length() < 3)
        {
            _nameText.setError("At least 3 characters");
            valid = false;
        }
        else
            _nameText.setError (null);

        // Email Validity Check
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            _emailText.setError("Enter a valid email address");
            valid = false;
        } else
            _emailText.setError (null);

        // Mobile Validity Check
        if (mobile.isEmpty() || mobile.length() != 10) {
            _mobileText.setError("Enter a valid mobile number");
            valid = false;
        } else
            _mobileText.setError (null);

        // Password Validity Check
        if (password.isEmpty() || password.length() < 4 || password.length() > 15) {
            _passwordText.setError("between 4 and 15 characters");
            valid = false;
        } else
            _passwordText.setError(null);

        return valid;
    }
}
