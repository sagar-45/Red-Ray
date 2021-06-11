package com.example.redray_final;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignIn_activity extends AppCompatActivity {
    TextView gosignUp;
    Button sign_me_in;
    EditText username,add,landmark1,password,email,bloodg,no,pin;
    private FirebaseAuth mAuth;
    String Uid;

    /***********************/



    /***********************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_activity);
        gosignUp=findViewById(R.id.gosignup);
        sign_me_in = findViewById(R.id.sign_me_in);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() !=null){
            finish();
        }

        gosignUp=findViewById(R.id.gosignup);
        sign_me_in = findViewById(R.id.sign_me_in);
        username=findViewById(R.id.username1);
        add=findViewById(R.id.add);
        landmark1=findViewById(R.id.landmark);
        pin=findViewById(R.id.pin);
        password=findViewById(R.id.password);
        email=findViewById(R.id.email);
        bloodg=findViewById(R.id.bloodg);
        no=findViewById(R.id.no);


        gosignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SignIn_activity.this,SignUp_activity.class);
                startActivity(intent);
                finish();
            }
        });
        sign_me_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String usern=username.getText().toString();
                String pass=password.getText().toString();
                String p_add=add.getText().toString();
                String land_m=landmark1.getText().toString();
                String pin_code=pin.getText().toString();
                String email_id=email.getText().toString();
                String bg=bloodg.getText().toString().toUpperCase();
                String mo_n =no.getText().toString();

                if(usern.isEmpty())
                {
                    username.setError("Please Enter UserName");
                }
                else if(pass.isEmpty())
                {
                    password.setError("Please Enter Password");
                }
                else  if(p_add.isEmpty())
                {
                    add.setError("Please Enter Permanent address");
                }
                else if(land_m.isEmpty())
                {
                    landmark1.setError("Please Enter Landmark");
                }
                else if(pin_code.isEmpty())
                {
                    pin.setError("Please Enter PinCode");
                }
                else if(email_id.isEmpty())
                {
                    email.setError("Please Enter Email-ID");
                }
                else if(bg.isEmpty())
                {
                    bloodg.setError("Please Enter Blood-Group");
                }
                else if(mo_n.isEmpty())
                {
                    no.setError("Please Enter Mobile Number");
                }
                else if(pass.length()<6)
                {
                    password.setError("Password at least 6 Characters");
                }
                else if(!(bg.equals("A+")) && !(bg.equals("A-")) && !(bg.equals("B+")) && !(bg.equals("B-")) && !(bg.equals("AB+")) &&
                        !(bg.equals("AB-")) && !(bg.equals("O+")) && !(bg.equals("O-"))){
                    bloodg.setError("Please Enter Valid Blood Group");
                }
                else if((pin_code.length()<6)  || (pin_code.length()>6))
                {
                    pin.setError("Pin Code have 6 Characters");
                }
                else if(mo_n.length()<10 || mo_n.length()>10)
                {
                    no.setError("Mobile Number have 10 digits" );
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(email_id).matches())
                {
                    email.setError("Please Enter a valid Email Address");
                }
                else
                {

                    mAuth.createUserWithEmailAndPassword(email_id, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override

                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {


                                Map<String,Object> data=new HashMap<>();
                                data.put("username",usern);
                                data.put("password",pass);
                                data.put("perment_add",p_add);

                                db.collection(bg).document(pin_code).collection(land_m).document(mo_n).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(SignIn_activity.this,"Login Success",Toast.LENGTH_LONG).show();
                                    }
                                });
                                String userid=mAuth.getCurrentUser().getUid();
                                Map<String,Object> data_user=new HashMap<>();
                                data_user.put("username",usern);
                                data_user.put("password",pass);
                                data_user.put("perment_add",p_add);
                                data_user.put("landmark",land_m);
                                data_user.put("pin_code",pin_code);
                                data_user.put("email",email_id);
                                data_user.put("bloodgroup",bg);
                                data_user.put("mo_n",mo_n);
                                db.collection("Users").document(userid).set(data_user);
                                Intent intent=new Intent(SignIn_activity.this,MainActivity2.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(SignIn_activity.this, "Login fail", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

}