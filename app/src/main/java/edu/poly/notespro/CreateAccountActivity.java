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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class CreateAccountActivity extends AppCompatActivity {
    EditText edtEmail, edtPass, edtConfirm;
    Button btnCtreate;
    TextView tvlogin;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        edtEmail = (EditText) findViewById(R.id.edtemail);
        edtPass = (EditText) findViewById(R.id.edtPassword);
        edtConfirm = (EditText) findViewById(R.id.edtConfirm);
        progressBar = (ProgressBar) findViewById(R.id.progress_Bar);
        tvlogin = (TextView) findViewById(R.id.tvLogin);
        btnCtreate = (Button) findViewById(R.id.btnCreateAccount);

        btnCtreate.setOnClickListener(v -> creaAccount());
        tvlogin.setOnClickListener(v -> finish());

    }

     void creaAccount() {
        String email = edtEmail.getText().toString();
        String pass = edtPass.getText().toString();
        String confirm = edtConfirm.getText().toString();

        boolean inValidated = ktraXacThuc(email, pass, confirm);

        if(!inValidated){
            return;
        }

    createAccountInFireBase(email,pass);

    }

    void  createAccountInFireBase(String email, String password){
        chageInProgress(true);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(CreateAccountActivity.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        chageInProgress(false);
                        if (task.isSuccessful()){
                            Toast.makeText(CreateAccountActivity.this, "Succesfully create account, ckeck email to verify", Toast.LENGTH_SHORT).show();
                            firebaseAuth.getCurrentUser().sendEmailVerification();
                            firebaseAuth.signOut();
                            finish();
                        }
                        else{
                            Toast.makeText(CreateAccountActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    void chageInProgress(boolean inprogress){
        if(inprogress){
            progressBar.setVisibility(View.VISIBLE);
            btnCtreate.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            btnCtreate.setVisibility(View.VISIBLE);
        }
    }

    boolean ktraXacThuc(String email, String pass, String confirm){

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            edtEmail.setError("Email is invalid");
            return false;
        }
        if (pass.length() <6){
            edtPass.setError("Password length is invarlid");
            return false;
        }
        if(!pass.equals(confirm)){
            edtConfirm.setError("Password not matched");
        }
        return true;
    }

}