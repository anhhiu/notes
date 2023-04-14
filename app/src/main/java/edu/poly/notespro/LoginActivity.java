package edu.poly.notespro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    EditText edtEmail, edtPass;
    Button btnLogin;
    TextView tvCreate;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edtEmail = (EditText) findViewById(R.id.edtemail);
        edtPass = (EditText) findViewById(R.id.edtPassword);
        progressBar = (ProgressBar) findViewById(R.id.progress_Bar);
        tvCreate = (TextView) findViewById(R.id.tvCreate);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> loginUser());
        tvCreate.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, CreateAccountActivity.class)));

    }

     void loginUser() {
         String email = edtEmail.getText().toString();
         String pass = edtPass.getText().toString();

         boolean inValidated = ktraXacThuc(email, pass);

         if(!inValidated){
             return;
         }

         LoginAccountInFireBase(email,pass);

     }

     void LoginAccountInFireBase(String email, String pass) {
         FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
         chageInProgress(true);

         firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
             @Override
             public void onComplete(@NonNull Task<AuthResult> task) {

                 chageInProgress(false);

                 if (task.isSuccessful()){
                     //login is success

                     if (firebaseAuth.getCurrentUser().isEmailVerified()){
                         // go to mainactivity
                         startActivity(new Intent(LoginActivity.this, MainActivity.class));
                     }else{
                         Utility.showToast(LoginActivity.this, "Email not verified, Please verify your email.");
                     }
                 }else{
                     Utility.showToast(LoginActivity.this, task.getException().getLocalizedMessage());
                 }


             }
         });
    }


    void chageInProgress(boolean inprogress){
        if(inprogress){
            progressBar.setVisibility(View.VISIBLE);
            btnLogin.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            btnLogin.setVisibility(View.VISIBLE);
        }
    }

    boolean ktraXacThuc(String email, String pass){

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            edtEmail.setError("Email is invalid");
            return false;
        }
        if (pass.length() <6){
            edtPass.setError("Password length is invarlid");
            return false;
        }
        return true;
    }

}