package com.example.sns_project.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sns_project.R;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener
{
    private static final String TAG = "Sign_up";
    //비밀번호 정규식
    private static final Pattern PSSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{4,16}$");
    //private DocumentReference db;



    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private FirebaseAuth mAuth;
    private String mVerificationId;
    public static FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseFirestore db;



    public SignUpActivity() {}
    EditText sign_email;
    EditText sign_password;
    EditText sign_password2;
    Button btn_sign_in;
    TextView tv_massege;
    ProgressDialog progressDialog;
    DatePicker datePicker;
    TextView tv_birth;
    EditText ed_name;
    EditText ed_phonenum;   // 핸드폰 번호와 코드 선언
    TextView ed_code; // 인증 코드 선언
    Button sent;    // 인증번호 보내기 버튼 선언
    Button Verify; // 인증코드 검사

    //initializing firebase auth object
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        sign_email = findViewById(R.id.sign_Email);
        sign_password = findViewById(R.id.sign_Password);
        sign_password2 = findViewById(R.id.sign_Password2);
        btn_sign_in = findViewById(R.id.btn_sign_in);
        tv_massege = findViewById(R.id.textviewMessage);
        ed_name = findViewById(R.id.ed_name);
        ed_phonenum = findViewById(R.id.ed_phonenum);
        ed_code = findViewById(R.id.ed_code);

        progressDialog = new ProgressDialog(this);
        //define firebase object
        firebaseAuth = FirebaseAuth.getInstance();
        btn_sign_in.setOnClickListener(this);
        datePicker = findViewById(R.id.datepicker);
        tv_birth = findViewById(R.id.tv_birthday);


        //생년월일 입력 달력
        datePicker.init(datePicker.getYear(),datePicker.getMonth(),datePicker.getDayOfMonth(),new DatePicker.OnDateChangedListener()
        {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                // TODO Auto-generated method stub
                tv_birth.setText(String.format("%d/%d/%d", year,monthOfYear + 1, dayOfMonth));
            }
        });
        ed_phonenum =findViewById(R.id.ed_phonenum);
        ed_code =findViewById(R.id.ed_code);

        sent =findViewById(R.id.sent);
        Verify =findViewById(R.id.verify);

        callback_verificvation();

        mAuth = FirebaseAuth.getInstance();
        sent.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String num=ed_phonenum.getText().toString();
                startPhoneNumberVerification(num);    // call function for receive OTP 6 digit code
                final Toast tt = Toast.makeText(getApplicationContext(), "인증번호를 보냈습니다.", Toast.LENGTH_LONG);
                tt.show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    { tt.cancel(); }
                },1000);
                Verify.setEnabled(false);

            }
        });
        Verify.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String code=ed_code.getText().toString();
                verifyPhoneNumberWithCode(mVerificationId,code);            //call function for verify code
                final Toast tt1 = Toast.makeText(getApplicationContext(), "인증번호 검사중.", Toast.LENGTH_LONG);
                tt1.show();
                Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable()
                {
                    @Override
                    public void run() {
                        tt1.cancel();
                    }
                },1000);
            }
        });
    }

    private void startPhoneNumberVerification(String phoneNumber)
    {
        // [START start_phone_auth]
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                120,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
        // [END start_phone_auth]
        final Toast tt3 = Toast.makeText(this, "전화번호 인증시작", Toast.LENGTH_LONG);
        tt3.show();
        Handler handler3 = new Handler();
        handler3.postDelayed(new Runnable()
        {
            @Override
            public void run() {
                tt3.cancel();
            }
        },1000);

    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential)
    {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = task.getResult().getUser();
                            final Toast tt4 = Toast.makeText(getApplicationContext(), "로그인 성공", Toast.LENGTH_LONG);
                            tt4.show();
                            Handler handler4 = new Handler();
                            handler4.postDelayed(new Runnable()
                            {
                                @Override
                                public void run() { tt4.cancel(); }
                            },1000);
                            // [START_EXCLUDE]

                            // [END_EXCLUDE]
                        } else
                        {
                            // Sign in failed, display a message and update the UI
                            final Toast tt5 = Toast.makeText(getApplicationContext(), "로그인 실패", Toast.LENGTH_LONG);
                            tt5.show();
                            Handler handler5 = new Handler();
                            handler5.postDelayed(new Runnable()
                            {
                                @Override
                                public void run() { tt5.cancel(); }
                            },1000);
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException)
                            {
                                // The verification code entered was invalid
                                // [START_EXCLUDE silent]
                                final Toast tt6 = Toast.makeText(getApplicationContext(), "유효하지 않은 번호입니다.", Toast.LENGTH_LONG);
                                tt6.show();
                                Handler handler6 = new Handler();
                                handler6.postDelayed(new Runnable()
                                {
                                    @Override
                                    public void run() { tt6.cancel(); }
                                },1000);
                                // [END_EXCLUDE]
                            }
                            // [START_EXCLUDE silent]
                            // Update UI

                            // [END_EXCLUDE]
                        }
                    }
                });
    }
    private void verifyPhoneNumberWithCode(String verificationId, String code)
    {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential);
        final Toast tt7 = Toast.makeText(getApplicationContext(), "코드 검사", Toast.LENGTH_LONG);
        tt7.show();
        Handler handler7 = new Handler();
        handler7.postDelayed(new Runnable()
        {
            @Override
            public void run() { tt7.cancel(); }
        },1000);
    }
    private void callback_verificvation()
    {

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks()
        {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential)
            {
                // callback
                signInWithPhoneAuthCredential(credential);
                final Toast tt8 = Toast.makeText(getApplicationContext(), "번호 인증 성공", Toast.LENGTH_LONG);
                tt8.show();
                Handler handler8 = new Handler();
                handler8.postDelayed(new Runnable()
                {
                    @Override
                    public void run() { tt8.cancel(); }
                },1000);
            }

            @Override
            public void onVerificationFailed(FirebaseException e)
            {
                // phone number format is not valid.

                final Toast tt9 = Toast.makeText(getApplicationContext(), "번호 인증 실패", Toast.LENGTH_LONG);
                tt9.show();
                Handler handler9 = new Handler();
                handler9.postDelayed(new Runnable()
                {
                    @Override
                    public void run() { tt9.cancel(); }
                },1000);
                if (e instanceof FirebaseAuthInvalidCredentialsException)
                {
                    // Invalid request
                    // [START_EXCLUDE]
                    final Toast tt10 = Toast.makeText(getApplicationContext(), "Invalid request 요청실패", Toast.LENGTH_LONG);
                    tt10.show();
                    Handler handler10 = new Handler();
                    handler10.postDelayed(new Runnable()
                    {
                        @Override
                        public void run() { tt10.cancel(); }
                    },1000);
                    // [END_EXCLUDE]
                } else if (e instanceof FirebaseTooManyRequestsException)
                {
                    // The SMS quota for the project has been exceeded
                    // [START_EXCLUDE]
                    final Toast tt11 = Toast.makeText(getApplicationContext(), "exceeded 요청초과", Toast.LENGTH_LONG);
                    tt11.show();
                    Handler handler11 = new Handler();
                    handler11.postDelayed(new Runnable()
                    {
                        @Override
                        public void run() { tt11.cancel(); }
                    },1000);
                    // [END_EXCLUDE]
                }
                // Show a message and update the UI
            }

            @Override
            public void onCodeSent(String verificationId,PhoneAuthProvider.ForceResendingToken token)
            {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.

                final Toast tt12 = Toast.makeText(getApplicationContext(), "인증번호 발송", Toast.LENGTH_LONG);
                tt12.show();
                Handler handler12 = new Handler();
                handler12.postDelayed(new Runnable()
                {
                    @Override
                    public void run() { tt12.cancel(); }
                },1000);
                Verify.setEnabled(true);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;

                // Update UI

            }
        };
    }
    //Firebase creating a new user
    private void registerUser()
    {
        //사용자가 입력하는 정보를 가져온다.
        String email = sign_email.getText().toString().trim();
        String password = sign_password.getText().toString().trim();
        String password2 = sign_password2.getText().toString().trim();
        String username = ed_name.getText().toString().trim().trim();
        String birthday = tv_birth.getText().toString().trim();
        String phonenum = ed_phonenum.getText().toString().trim();
        String YearStr = Integer.toString(datePicker.getYear());
        String dateMonth = Integer.toString(datePicker.getMonth()+1);
        String dayMonth = Integer.toString(datePicker.getDayOfMonth());
        String Datetest = YearStr+dateMonth+dayMonth;
        //가입정보에 비어있는게 있는 확인

        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "Email을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "password를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password2))
        {
            Toast.makeText(this, "password를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!password2.equals(password))
        {
            Toast.makeText(this, "password가 일치 하지않습니다.", Toast.LENGTH_SHORT).show();
            return;
            // 비밀번호 일치 여부 확인
        }
        if(TextUtils.isEmpty(username))
        {
            Toast.makeText(this, "이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(birthday))
        {
            Toast.makeText(this, "생년월일을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(phonenum))
        {
            Toast.makeText(this, "전화번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        //email과 password가 제대로 입력되어 있다면 계속 진행된다.
        progressDialog.setMessage("등록중입니다. 기다려 주세요.");
        progressDialog.show();
        progressDialog.dismiss();
        //creating a new user
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if(task.isSuccessful())
                {
                    Log.i(TAG, "createUserWithEmail:success");
                    final Toast tt13 = Toast.makeText(SignUpActivity.this, "등록 성공", Toast.LENGTH_LONG);
                    tt13.show();
                    Handler handler13 = new Handler();
                    handler13.postDelayed(new Runnable()
                    {
                        @Override
                        public void run() { tt13.cancel(); }
                    },1000);
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    String UID = user.getUid();

                    db = FirebaseFirestore.getInstance();
                    //DB data 넣기
                    LinkedHashMap<String, Object> user2 = new LinkedHashMap<String, Object>();
                    user2.put("email", sign_email.getText().toString().trim());
                    user2.put("password", sign_password.getText().toString().trim());
                    user2.put("username", ed_name.getText().toString().trim().trim());
                    user2.put("birth", Integer.parseInt(Datetest));
                    user2.put("phone", ed_phonenum.getText().toString().trim());
                    user2.put("photoUrl","image/tt.jpg");
                    db.collection("users").document(UID).set(user2);

                    Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                else
                {
                    //에러 발생시
                    tv_massege.setText("에러유형 \n -이미 등록된 이메일이거나 \n -암호 최소 6자리 이상 \n -서버에러");
                    final Toast tt14 = Toast.makeText(SignUpActivity.this, "등록 에러", Toast.LENGTH_LONG);
                    tt14.show();
                    Handler handler14 = new Handler();
                    handler14.postDelayed(new Runnable()
                    {
                        @Override
                        public void run() { tt14.cancel(); }
                    },1000);
                    Log.e(TAG, "createUserWithEmail:failure", task.getException());
                    Toast.makeText(SignUpActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //firebase db 초기화

        db = FirebaseFirestore.getInstance();
        //DB data 넣기
        LinkedHashMap<String, Object> user2 = new LinkedHashMap<String, Object>();
        user2.put("email", sign_email.getText().toString().trim());
        user2.put("password", sign_password.getText().toString().trim());
        user2.put("username", ed_name.getText().toString().trim().trim());
        user2.put("birth", Integer.parseInt(Datetest));
        user2.put("phone", ed_phonenum.getText().toString().trim());






/*
        //users 디렉터리 만들기
        db.collection("users").document(UID).set(user)
        .addOnSuccessListener(new OnSuccessListener<DocumentReference>()
        {
            @Override
            public void onSuccess(DocumentReference documentReference)
            {

            }
        })
        .addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                Log.w(TAG, "Error adding document", e);
                Toast.makeText(Sign_up.this, "등록 실패", Toast.LENGTH_SHORT).show();
            }
        });*/
        db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task)
            {
                if(task.isSuccessful())
                {
                    for(QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult()))
                    {
                        Log.d(TAG, document.getId() + "=>" + document.getData());
                    }
                }
                else
                {
                    Log.w(TAG, "Error getting document.", task.getException());
                }
            }
        });
    }


    //버튼 클릭 리스너
    @Override
    public void onClick(View view)
    {
        if(view == btn_sign_in)
        {
            registerUser();
            Toast.makeText(this, "등록", Toast.LENGTH_SHORT).show();
            //finish();
        }
    }

}