package com.example.sns_project.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sns_project.R;
import com.example.sns_project.activity.ModifyPostActivity;
import com.example.sns_project.activity.PostDetailActivity;
import com.example.sns_project.adapter.FirestoreAdapter;
import com.example.sns_project.models.Post;
import com.example.sns_project.models.User;
import com.example.sns_project.viewholder.PostViewHolder;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

public abstract class HomeFragment extends Fragment implements View.OnCreateContextMenuListener{

    private static final String TAG = "PostListFragment";
    private static final String REQUIRED = "Required";

    private PostViewHolder postInfo;

    private EditText mTitleField;
    private EditText mBodyField;
    private FloatingActionButton mSubmitButton;
    private DocumentReference mPostReference;
    private DocumentReference mPostReference1;




    // [START define_database_reference]
    private FirebaseFirestore db;
    // [END define_database_reference]

    private PostAdapter mAdapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;
    private View TEST;

    final String uid = getUid();
    final String id = getID();

    private Context context1;


    public HomeFragment() {}



    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.activity_main, container, false);

        // [START create_database_reference]
        db = FirebaseFirestore.getInstance();
        // [END create_database_reference]

        mRecycler = rootView.findViewById(R.id.messagesList);

        mRecycler.setHasFixedSize(true);
        registerForContextMenu(mRecycler);

        return rootView;
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Set up Layout Manager, reverse layout
        mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);

        // Set up FirebaseRecyclerAdapter with the Query
        Query postsQuery = getQuery(db);

        mAdapter = new PostAdapter(postsQuery);
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecycler.setAdapter(mAdapter);

    }


    class PostAdapter extends FirestoreAdapter<PostViewHolder> {

        PostAdapter(Query query) {
            super(query);
        }

        @Override
        public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new PostViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_post, parent, false));
        }

        @Override
        public void onBindViewHolder(PostViewHolder viewHolder, int position) {
            DocumentSnapshot documentSnapshot = getSnapshot(position);
            Post post = documentSnapshot.toObject(Post.class);
            FirebaseStorage storage = FirebaseStorage.getInstance("gs://test-30ba7.appspot.com/");
            StorageReference storageRef = storage.getReference();

            final String postKey = documentSnapshot.getId();
            mPostReference1 = FirebaseFirestore.getInstance().collection("users").document(post.uid);
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

                            Glide.with(getContext().getApplicationContext())
                                    .load(uri)
                                    .into(viewHolder.mauthorView);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //이미지 로드 실패시
                            Toast.makeText(getActivity(), "실패", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            });

            // Set click listener for the whole post view
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Launch PostDetailActivity
                    Intent intent = new Intent(getActivity(), PostDetailActivity.class);
                    intent.putExtra(PostDetailActivity.EXTRA_POST_KEY, postKey);
                    intent.putExtra(PostDetailActivity.puid, post.uid);
                    startActivity(intent);
                }

            });

            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View view) {

                    viewHolder.itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener(){
                        @Override
                        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                            HomeFragment.super.onCreateContextMenu(contextMenu,view,contextMenuInfo);


                            MenuItem edit = contextMenu.add(Menu.NONE, R.id.edit,Menu.NONE,"수정");
                            MenuItem delete = contextMenu.add(Menu.NONE, R.id.delete,Menu.NONE,"삭제");

                            if(post.uid.equals(getUid())){
                                edit.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                                    @Override
                                    public boolean onMenuItemClick(MenuItem menuItem) {
                                        Intent intent1 = new Intent(getActivity(), ModifyPostActivity.class);
                                        intent1.putExtra(ModifyPostActivity.EXTRA_POST_KEY, postKey);
                                        startActivity(intent1);
                                        return false;
                                    }
                                });
                                delete.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                                    @Override
                                    public boolean onMenuItemClick(MenuItem menuItem)
                                    {
                                        db.collection("posts").document(postKey)
                                                .delete()
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.w(TAG, "Error deleting document", e);
                                                    }
                                                });
                                        return false;
                                    }
                                });
                            }
                            else{
                                edit.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                                    @Override
                                    public boolean onMenuItemClick(MenuItem menuItem) {
                                        Toast.makeText(getActivity(), "작성자가 아닙니다.", Toast.LENGTH_SHORT).show();
                                        return false;
                                    }
                                });
                                delete.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                                    @Override
                                    public boolean onMenuItemClick(MenuItem menuItem)
                                    {
                                        Toast.makeText(getActivity(), "작성자가 아닙니다.", Toast.LENGTH_SHORT).show();
                                        return false;
                                    }
                                });
                            }
                        }
                    });
                    return false;
                }
            });


            // Determine if the current user has liked this post and set UI accordingly
            if (post.stars.indexOf(getUid())>-1)
            {
                //if (post.stars.containsKey(getUid())) {
                viewHolder.starView.setImageResource(R.drawable.ic_toggle_star_24);
            }
            else
            {
                viewHolder.starView.setImageResource(R.drawable.ic_toggle_star_outline_24);
            }

            // Bind Post to ViewHolder, setting OnClickListener for the star button
            viewHolder.bindToPost(post, new View.OnClickListener()
            {
                @Override
                public void onClick(View starView) {
                    db.collection("posts").document(postKey).get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    Post post = documentSnapshot.toObject(Post.class);
                                    if (post.stars.indexOf(getUid())==-1) {
                                        post.stars.add(getUid());
                                    } else {
                                        post.stars.remove(getUid());
                                    }
                                    post.starCount = post.stars.size();
                                    documentSnapshot.getReference().set(post);
                                }
                            });
                }
            });

        }
    }


    @Override
    public void onStart() {
        super.onStart();
        if (mAdapter != null) {
            mAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAdapter != null) {
            mAdapter.stopListening();
        }
    }





    public String getUid() {
        return Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    }
    public String getID()
    {
        return Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getDisplayName();
    }


    public abstract Query getQuery(FirebaseFirestore databaseReference);

}