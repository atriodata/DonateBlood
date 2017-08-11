package com.atrio.donateblood;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import dmax.dialog.SpotsDialog;

public class VerifyOTP extends AppCompatActivity {

    private EditText et_verify;
    private Button btn_resend, btn_Verify;
    private String phn_no, mVerificationId, otp;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    FirebaseAuth mAuth;
    FirebaseUser User;
    private SpotsDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);
        et_verify = (EditText) findViewById(R.id.et_otp);
        btn_resend = (Button) findViewById(R.id.btn_resend);
        btn_Verify = (Button) findViewById(R.id.btn_verify);

        dialog = new SpotsDialog(VerifyOTP.this, R.style.Custom);

        Intent i = getIntent();
        phn_no = i.getStringExtra("phn_number");
        Log.i("onVerify", "" + phn_no);

        mAuth = FirebaseAuth.getInstance();
        User = mAuth.getCurrentUser();
//        fireBasePhLogin(phn_no);

        dialog.show();
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                dialog.dismiss();
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
//                Log.w(TAG, "onVerificationFailed", e);
               /* if (progressDialog != null) {
                    dismissProgressDialog(progressDialog);
                }*/
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // ...
                    Log.d("Invalid request", "Invalid request");
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // ...
//                    Log.d("Error", "The SMS quota for the project has been exceeded");
                    dialog.dismiss();
                    Toast.makeText(VerifyOTP.this, "sms_qutoa_exceeded_alert", Toast.LENGTH_SHORT).show();
//                    llPhNumberLayout.setVisibility(View.GONE);
//                    llOTPNumberLayout.setVisibility(View.VISIBLE);
                }

                // Show a message and update the UI
                // ...
            }

            @Override
            public void onCodeAutoRetrievalTimeOut(String s) {
                super.onCodeAutoRetrievalTimeOut(s);
                dialog.dismiss();
                otp = et_verify.getText().toString().trim();
                if (TextUtils.isEmpty(otp)) {
                    Toast.makeText(VerifyOTP.this, "Enter OTP", Toast.LENGTH_SHORT).show();
                } else {
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(s, otp);
                    signInWithPhoneAuthCredential(credential);
                }
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                dialog.dismiss();
                mVerificationId = verificationId;
                final String mResendToken = token.toString();
                Toast.makeText(VerifyOTP.this, "OTP is send", Toast.LENGTH_SHORT).show();
                // ...
            }
        };
        btn_Verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                if (TextUtils.isEmpty(et_verify.getText().toString().trim())) {
                    dialog.dismiss();
                    Toast.makeText(VerifyOTP.this, "Enter OTP", Toast.LENGTH_SHORT).show();
                } else {
                    otp = et_verify.getText().toString().trim();
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, otp);
                    signInWithPhoneAuthCredential(credential);
                }
            }
        });
        btn_resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                dialog.show();
                fireBasePhLogin(phn_no);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        fireBasePhLogin(phn_no);

    }

    private void fireBasePhLogin(String phoneNumber) {
        Log.i("onCodeSent422:", "" + phoneNumber);

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            User = task.getResult().getUser();
                            Log.d("", "onComplete: " + User);
                            dialog.dismiss();
                            Toast.makeText(VerifyOTP.this, "Phone number verified", Toast.LENGTH_SHORT).show();
                            // ...
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                dialog.dismiss();
                                Toast.makeText(VerifyOTP.this, "wrong_verification_code_alert", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
}
