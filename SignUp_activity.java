package com.example.redray_final;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignUp_activity extends AppCompatActivity {
    Button sign_me_up;
    EditText username,up_email,up_pass;
    private FirebaseAuth mAuth;
    FirebaseFirestore fstore;
    TextView forgot_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_activity);
        sign_me_up= findViewById(R.id.sign_me_up);

        up_email=findViewById(R.id.up_email);
        up_pass=findViewById(R.id.up_pass);
        forgot_pass=findViewById(R.id.forgot_pass);
        mAuth = FirebaseAuth.getInstance();

        sign_me_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String email_id=up_email.getText().toString();
                String password=up_pass.getText().toString();

                if(email_id.isEmpty())
                {
                    up_email.setError("Please Enter Email Address");
                }
                else if(password.isEmpty())
                {
                    up_pass.setError("Please Enter Password");
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(email_id).matches())
                {
                    up_email.setError("Please Enter Valid Email Address");
                }
                else if(password.length()<6)
                {
                    up_pass.setError("Password have at least 6 characters");
                }
                else
                {
                    mAuth.signInWithEmailAndPassword(email_id, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        String userid=mAuth.getCurrentUser().getUid();
                                        fstore=FirebaseFirestore.getInstance();
                                            DocumentReference dr = fstore.collection("Users").document(userid);
                                            dr.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        DocumentSnapshot documentSnapshot=task.getResult();
                                                        if(documentSnapshot.exists()) {
                                                            Intent intent = new Intent(SignUp_activity.this, MainActivity2.class);
                                                            startActivity(intent);
                                                            finish();
                                                        }
                                                    }
                                                }
                                            });
                                            DocumentReference dr1 = fstore.collection("hospital").document(userid);
                                            dr1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        DocumentSnapshot documentSnapshot=task.getResult();
                                                        if(documentSnapshot.exists()) {
                                                            Intent intent = new Intent(SignUp_activity.this, MainActivity3.class);
                                                            startActivity(intent);
                                                            finish();
                                                        }
                                                    }

                                                }
                                            });


                                    } else {
                                        Toast.makeText(SignUp_activity.this, "Sign Up Fail", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                }
            }

        });
        forgot_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText resetpass=new EditText(SignUp_activity.this);
                AlertDialog.Builder passreset=new AlertDialog.Builder(SignUp_activity.this);

                passreset.setTitle(Html.fromHtml("<font color='gray'>Password Forgot</font>"));
                passreset.setMessage(Html.fromHtml("<font color='gray'>Enter your Email Id</font>"));
                resetpass.setTextColor(Color.rgb(128,128,128));
                passreset.setView(resetpass);
                passreset.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String mail=resetpass.getText().toString();
                        if(mail.equals("")){
                            Toast.makeText(SignUp_activity.this, "Enter Email Id in Forgot password", Toast.LENGTH_SHORT).show();
                        }
                        else {

                            mAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(SignUp_activity.this, "Reset Link send on your Email Id", Toast.LENGTH_LONG).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(SignUp_activity.this, "Reset Link is not send", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                });
                passreset.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                passreset.create().show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(SignUp_activity.this,SignIn_activity.class);
        startActivity(intent);
    }
}