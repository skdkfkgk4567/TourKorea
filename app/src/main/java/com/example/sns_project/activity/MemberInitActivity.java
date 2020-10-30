package com.example.sns_project.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;
import com.example.sns_project.R;
import com.example.sns_project.models.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.example.sns_project.Util.showToast;

public class MemberInitActivity extends BaseActivity {
    private static final String TAG = "MemberInitActivity";
    private ImageView profileImageVIew;
    private RelativeLayout loaderLayout;
    private RelativeLayout buttonBackgroundLayout;
    private String profilePath;
    private FirebaseUser user;
    private String imgurl1;
    private String mdname;
    private String mdphoneNumber;
    private String mdbirth;
    private String mdaddress;
    private EditText mnameField;
    private EditText mphoneField;
    private DocumentReference mPostReference;
    private FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
    private String userT;
    private String imgurl2;
    private Uri filePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_init);
        loaderLayout = findViewById(R.id.loaderLyaout);
        profileImageVIew = findViewById(R.id.profileImageView);
        buttonBackgroundLayout = findViewById(R.id.buttonsBackgroundLayout);

        buttonBackgroundLayout.setOnClickListener(onClickListener);
        profileImageVIew.setOnClickListener(onClickListener);
        mnameField = findViewById(R.id.nameEditText);
        mphoneField = findViewById(R.id.phoneNumberEditText);

        findViewById(R.id.checkButton).setOnClickListener(onClickListener);
        findViewById(R.id.gallery).setOnClickListener(onClickListener);
        findViewById(R.id.picture).setOnClickListener(onClickListener);
    }

    public void onStart() {
        super.onStart();
        userT=user1.toString();
        FirebaseStorage storage = FirebaseStorage.getInstance("gs://sns-project-4a58d.appspot.com/");
        StorageReference storageRef = storage.getReference();
        mPostReference = FirebaseFirestore.getInstance().collection("users").document(getUid());
        mPostReference.addSnapshotListener((snapshot, e) -> {
            if (e != null) {
                Log.w(TAG, "Listen failed.", e);
                return;
            }
            User user12 = snapshot.toObject(User.class);
            // [START_EXCLUDE]
            mnameField.setText(user12.username);
            mphoneField.setText(user12.phone);
            if(profilePath !=null){

            }
            else{
                if(user12.photoUrl !=null) {
                    storageRef.child(user12.photoUrl).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                        @Override
                        public void onSuccess(Uri uri) {
                            //이미지 로드 성공시
                            Glide.with(getApplicationContext())
                                    .load(uri)
                                    .into(profileImageVIew);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //이미지 로드 실패시
                            Toast.makeText(getApplicationContext(), "실패", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else{

                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private String getRealPath(Uri contentUri)
    {
        if(contentUri.getPath().startsWith("/storage"))
        {
            return contentUri.getPath();
        }

        String id = DocumentsContract.getDocumentId(contentUri).split(":")[1];
        String[] columns = {MediaStore.Files.FileColumns.DATA};
        String selection = MediaStore.Files.FileColumns._ID + " = " + id;
        Cursor cursor = getContentResolver().query(MediaStore.Files.getContentUri("external"),columns,selection,null,null);
        try {
            int columsindex = cursor.getColumnIndex(columns[0]);
            if(cursor.moveToFirst())
            {
                return cursor.getString(columsindex);
            }
        }
        finally {
            cursor.close();
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //request코드가 0이고 OK를 선택했고 data에 뭔가가 들어 있다면
        if(requestCode == 0 && resultCode == RESULT_OK){
            filePath = data.getData();
            profilePath = getRealPath(filePath);
            Log.d(TAG, "uri:" + String.valueOf(filePath));
            try {
                //Uri 파일을 Bitmap으로 만들어서 ImageView에 집어 넣는다.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                profileImageVIew.setImageBitmap(bitmap);
                buttonBackgroundLayout.setVisibility(View.GONE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.checkButton:
                    storageUploader();
                    break;
                case R.id.profileImageView:
                    buttonBackgroundLayout.setVisibility(View.VISIBLE);
                    break;
                case R.id.buttonsBackgroundLayout:
                    buttonBackgroundLayout.setVisibility(View.GONE);
                    break;
                case R.id.gallery:
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "이미지를 선택하세요."), 0);
                    break;
                case R.id.picture:
                    myStartActivity(CameraActivity.class);
                    break;
            }
        }
    };

    private void storageUploader() {
        final String name = ((EditText) findViewById(R.id.nameEditText)).getText().toString();
        final String phoneNumber = ((EditText) findViewById(R.id.phoneNumberEditText)).getText().toString();
        final String birth = ((EditText) findViewById(R.id.birthDayEditText)).getText().toString();
        final String address = ((EditText) findViewById(R.id.addressEditText)).getText().toString();
        mdname = name;
        mdphoneNumber = phoneNumber;
        mdaddress = address;
        mdbirth = birth;

        if (name.length() > 0 && phoneNumber.length() > 9) {
            loaderLayout.setVisibility(View.VISIBLE);
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            user = FirebaseAuth.getInstance().getCurrentUser();

            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMHH_mmss");
            Date now = new Date();
            String filename = formatter.format(now);

            final StorageReference mountainImagesRef = storage.getReferenceFromUrl("gs://sns-project-4a58d.appspot.com/").child("users/" + user.getUid() +"/" +filename+"/profileImage.jpg");
            imgurl2 = "users/" + user.getUid() +"/" +filename+"/profileImage.jpg";

            if (profilePath == null)
            {
                userInfo2();
                storeUploader2();
            } else
                {
                try {
                    InputStream stream = new FileInputStream(new File(profilePath));
                    UploadTask uploadTask = mountainImagesRef.putStream(stream);
                    uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            return mountainImagesRef.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                imgurl1 = downloadUri.toString();
                                userInfo1();
                                storeUploader1();
                            } else {
                                showToast(MemberInitActivity.this, "회원정보를 보내는데 실패하였습니다.");
                            }
                        }
                    });
                } catch (FileNotFoundException e) {
                    Log.e("로그", "에러: " + e.toString());
                }
            }
        } else {
            showToast(MemberInitActivity.this, "회원정보를 입력해주세요.");
        }
    }
    public Map<String,Object> userInfo1(){
        final String md1name = mdname;
        final String md1phone = mdphoneNumber;
        final String md1addr = mdaddress;
        final String md1birth = mdbirth;
        HashMap<String, Object> mdresult = new HashMap<>();
        mdresult.put("username", md1name);
        mdresult.put("phone", md1phone);
        mdresult.put("photoUrl",imgurl2);
        mdresult.put("address",md1addr);
        mdresult.put("birth",md1birth);

        return mdresult;
    }
    public Map<String,Object> userInfo2(){
        final String md1name = mdname;
        final String md1phone = mdphoneNumber;
        final String md1addr = mdaddress;
        final String md1birth = mdbirth;
        HashMap<String, Object> mdresult = new HashMap<>();
        mdresult.put("username", md1name);
        mdresult.put("phone", md1phone);
        mdresult.put("photoUrl",imgurl2);
        mdresult.put("address",md1addr);
        mdresult.put("birth",md1birth);

        return mdresult;
    }

    private void storeUploader1() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(user.getUid()).update(userInfo1())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        showToast(MemberInitActivity.this, "회원정보 등록을 성공하였습니다.");
                        loaderLayout.setVisibility(View.GONE);
                        myStartActivity(MainActivity.class);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showToast(MemberInitActivity.this, "회원정보 등록에 실패하였습니다.");
                        loaderLayout.setVisibility(View.GONE);
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }
    private void storeUploader2() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(user.getUid()).update(userInfo2())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        showToast(MemberInitActivity.this, "회원정보 등록을 성공하였습니다.");
                        loaderLayout.setVisibility(View.GONE);
                        myStartActivity(MainActivity.class);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showToast(MemberInitActivity.this, "회원정보 등록에 실패하였습니다.");
                        loaderLayout.setVisibility(View.GONE);
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }

    private void myStartActivity(Class c) {
        Intent intent = new Intent(this, c);
        startActivityForResult(intent, 0);
    }

}
