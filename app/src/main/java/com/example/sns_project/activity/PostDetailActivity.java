package com.example.sns_project.activity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sns_project.R;
import com.example.sns_project.models.Comment;
import com.example.sns_project.models.Post;
import com.example.sns_project.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PostDetailActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "PostDetailActivity";

    public static final String EXTRA_POST_KEY = "post_key";

    private DocumentReference mPostReference;
    private DocumentReference mPostReference1;
    private String mPostKey;
    private CommentAdapter mAdapter;
    private ArrayList<DocumentSnapshot> mSnapshots = new ArrayList<>();

    private TextView mAuthorView;
    private TextView mTitleView;
    private TextView mBodyView;
    private TextView mTimeView;
    private ImageView mImageView;
    private ImageView mImageView2;
    private EditText mCommentField;
    private Button mCommentButton;
    private ImageView mAuthorimageView;
    private RecyclerView mCommentsRecycler;
    private TextView ctimeView;
    private View pAL;
    private boolean isFabOpen = false;
    private Context mContext;
    private FirebaseFirestore db;
    public static final String puid = "puid";
    private String mpuid;
    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    Date time = new Date();
    public String commenttime = format1.format(time);




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_post_detail);

        db = FirebaseFirestore.getInstance();

        // Get post key from intent
        mPostKey = getIntent().getStringExtra(EXTRA_POST_KEY);
        if (mPostKey == null) {
            throw new IllegalArgumentException("Must pass EXTRA_POST_KEY");
        }
        mpuid = getIntent().getStringExtra(puid);
        if (mpuid == null) {
            throw new IllegalArgumentException("Must pass EXTRA_puid");
        }
        mContext = getApplicationContext();



        // Initialize Database
        mPostReference = FirebaseFirestore.getInstance().collection("posts").document(mPostKey);

        // Initialize Views
        mAuthorView = findViewById(R.id.postAuthor);
        mAuthorimageView = findViewById(R.id.postAuthorPhoto);
        mTitleView = findViewById(R.id.postTitle);
        mBodyView = findViewById(R.id.postBody);
        mTimeView = findViewById(R.id.posttime);
        mImageView = findViewById(R.id.postimg);
        mImageView2 = findViewById(R.id.postimg2);
        mCommentField = findViewById(R.id.fieldCommentText);
        mCommentButton = findViewById(R.id.buttonPostComment);
        mCommentsRecycler = findViewById(R.id.recyclerPostComments);
        pAL = findViewById(R.id.postAuthorLayout);



        mCommentButton.setOnClickListener(this);
        mCommentsRecycler.setLayoutManager(new LinearLayoutManager(this));

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mImageView2.setVisibility(View.VISIBLE);
                mImageView2.bringToFront();
            }
        });
        mImageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mImageView2.setVisibility(View.INVISIBLE);
            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseStorage storage = FirebaseStorage.getInstance("gs://sns-project-4a58d.appspot.com/");
        StorageReference storageRef = storage.getReference();
        // Add value event listener to the post
        // [START post_value_event_listener]
        mPostReference.addSnapshotListener((snapshot, e) -> {
            if (e != null) {
                Log.w(TAG, "Listen failed.", e);
                return;
            }
            Post post = snapshot.toObject(Post.class);

            // [START_EXCLUDE]
            mAuthorView.setText(post.author);
            mTitleView.setText(post.title);
            mBodyView.setText(post.body);
            mTimeView.setText(post.time1);


            if(post.imgurl !=null) {
                storageRef.child(post.imgurl).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                    @Override
                    public void onSuccess(Uri uri) {
                        //이미지 로드 성공시

                        Glide.with(getApplicationContext())
                                .load(uri)
                                .into(mImageView);
                        Glide.with(getApplicationContext())
                                .load(uri)
                                .into(mImageView2);
                        mImageView2.setVisibility(View.INVISIBLE);

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
                mImageView.setVisibility(View.INVISIBLE);
            }

        });

        mPostReference1 = FirebaseFirestore.getInstance().collection("users").document(mpuid);
        System.out.println("puid2"+mpuid);
        mPostReference1.addSnapshotListener((snapshot, e) -> {
            if (e != null) {
                Log.w(TAG, "Listen failed.", e);
                return;
            }
            User user12 = snapshot.toObject(User.class);
            // [START_EXCLUDE]
            if(user12.photoUrl !=null) {
                storageRef.child(user12.photoUrl).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                    @Override
                    public void onSuccess(Uri uri) {
                        //이미지 로드 성공시
                        Glide.with(getApplicationContext())
                                .load(uri)
                                .into(mAuthorimageView);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        //이미지 로드 실패시
                        Toast.makeText(getApplicationContext(), "실패", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        });
        // [END post_value_event_listener]

        // Keep copy of post listener so we can remove it when app stops

        // Listen for comments
        mAdapter = new CommentAdapter(mPostReference.collection("post-comments"));
        mCommentsRecycler.setAdapter(mAdapter);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAdapter != null) {
            mAdapter.cleanupListener();
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.buttonPostComment) {
            postComment();
        }
        else if (i == R.id.postimg){

        }
        else if (i == R.id.postimg2){

        }

    }


    private void postComment() {
        final String uid = getUid();
        DocumentReference docRef = FirebaseFirestore.getInstance().collection("users").document(uid);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);
                if (user == null) {
                    Log.e(TAG, "User " + uid + " is unexpectedly null");
                    Toast.makeText(PostDetailActivity.this,
                            "Error: could not fetch user.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Comment comment = new Comment(uid, user.username, mCommentField.getText().toString(),commenttime);
                    mPostReference.collection("post-comments").document().set(comment);

                    mCommentField.setText(null);
                }
            }
        });
    }

    private static class CommentViewHolder extends RecyclerView.ViewHolder {

        public TextView authorView;
        public TextView bodyView;
        public TextView ctimeView;
        public ImageView commentPhoto;


        CommentViewHolder(View itemView) {
            super(itemView);

            authorView = itemView.findViewById(R.id.commentAuthor);
            bodyView = itemView.findViewById(R.id.commentBody);
            ctimeView = itemView.findViewById(R.id.commenttime);
            commentPhoto = itemView.findViewById(R.id.commentPhoto);
        }
    }

    //private static class FirestoreAdapter extends RecyclerView.Adapter<CommentViewHolder> {
    private class CommentAdapter extends RecyclerView.Adapter<CommentViewHolder> {
        private List<String> mCommentIds = new ArrayList<>();
        private List<Comment> mComments = new ArrayList<>();

        private ListenerRegistration listenerRegistration;
        private DocumentReference mPostReference1;

        public CommentAdapter(Query query) {
            // Create child event listener
            // [START child_event_listener_recycler]
            EventListener childEventListener = new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot snapshots,
                                    @Nullable FirebaseFirestoreException e) {
                    if (e != null) {return;}
                    String commentKey;
                    int commentIndex;
                    Comment comment;

                    for (DocumentChange dc : snapshots.getDocumentChanges()) {
                        switch (dc.getType()) {
                            case ADDED:
                                // A new comment has been added, add it to the displayed list
                                comment = dc.getDocument().toObject(Comment.class);
                                // [START_EXCLUDE]
                                // Update RecyclerView
                                mCommentIds.add(dc.getDocument().getId());
                                mComments.add(comment);
                                notifyItemInserted(mComments.size() - 1);
                                break;
                            case MODIFIED:
                                // A comment has changed, use the key to determine if we are displaying this
                                // comment and if so displayed the changed comment.
                                comment = dc.getDocument().toObject(Comment.class);
                                commentKey = dc.getDocument().getId();
                                // [START_EXCLUDE]
                                commentIndex = mCommentIds.indexOf(commentKey);
                                if (commentIndex > -1) {
                                    // Replace with the new data
                                    mComments.set(commentIndex, comment);

                                    // Update the RecyclerView
                                    notifyItemChanged(commentIndex);
                                } else {
                                    Log.w(TAG, "onChildChanged:unknown_child:" + commentKey);
                                }
                                // [END_EXCLUDE]
                                break;
                            case REMOVED:
                                // A comment has changed, use the key to determine if we are displaying this
                                // comment and if so remove it.
                                commentKey = dc.getDocument().getId();
                                // [START_EXCLUDE]
                                commentIndex = mCommentIds.indexOf(commentKey);
                                if (commentIndex > -1) {
                                    // Remove data from the list
                                    mCommentIds.remove(commentIndex);
                                    mComments.remove(commentIndex);

                                    // Update the RecyclerView
                                    notifyItemRemoved(commentIndex);
                                } else {
                                    Log.w(TAG, "onChildRemoved:unknown_child:" + commentKey);
                                }
                                // [END_EXCLUDE]
                                break;
                        }
                    }

                }
            };
            // [END child_event_listener_recycler]
            listenerRegistration = query.addSnapshotListener(childEventListener);
        }

        @Override
        public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.item_comment, parent, false);
            return new CommentViewHolder(view);
        }

        @Override
        public void onBindViewHolder(CommentViewHolder holder, int position) {
            Comment comment = mComments.get(position);
            holder.authorView.setText(comment.author);
            holder.bodyView.setText(comment.text);
            holder.ctimeView.setText(comment.commenttime);
            System.out.println(comment.uid);

            FirebaseStorage storage = FirebaseStorage.getInstance("gs://sns-project-4a58d.appspot.com");
            StorageReference storageRef = storage.getReference();

            mPostReference1 = FirebaseFirestore.getInstance().collection("users").document(comment.uid);
            mPostReference1.addSnapshotListener((snapshot, e) -> {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }
                User user12 = snapshot.toObject(User.class);
                // [START_EXCLUDE]
                if(user12.photoUrl !=null) {
                    storageRef.child(user12.photoUrl).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                        @Override
                        public void onSuccess(Uri uri) {
                            //이미지 로드 성공시
                            Glide.with(getApplicationContext())
                                    .load(uri)
                                    .into(holder.commentPhoto);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //이미지 로드 실패시
                            Toast.makeText(getApplicationContext(), "실패", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            });

        }

        @Override
        public int getItemCount() {
            return mComments.size();
        }

        public void cleanupListener() {
            listenerRegistration.remove();
        }
    }

}
