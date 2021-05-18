package com.sc1prabin.neblog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sc1prabin.neblog.Data.BlogRecyclerAdapter;
import com.sc1prabin.neblog.Model.Blog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PostListActivity extends AppCompatActivity {

    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mDatabase;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    private  BlogRecyclerAdapter blogRecyclerAdapter;
    private List<Blog> blogList;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_list);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference().child("NBlog");
        //Reminder:just because of the the typo Blog instead of NBlog i was stuck here for more than 20 days it was like nightmare, this is real pain
        mDatabaseReference.keepSynced(true);

        blogList = new ArrayList<>();




        recyclerView =(RecyclerView) findViewById(R.id.recyclerViewId);

        recyclerView.setAdapter(blogRecyclerAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add:
            if(mUser != null && mAuth != null) {
                startActivity(new Intent(PostListActivity.this, AddPostActivity.class));
                finish();
            }
            break;
            case R.id.action_signout:
                if(mUser != null && mAuth != null) {
                mAuth.signOut();
                startActivity(new Intent(PostListActivity.this, MainActivity.class));
                finish();
            }

        }

        return super.onOptionsItemSelected(item);
    }
    //just above two methods allow us to inflate our menu ,so that it can be shown at the top of activity

    @Override
    protected void onStart() {
        super.onStart();
        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, String previousChildName) {
             Blog blog = snapshot.getValue(Blog.class);
             blogList.add(blog);
                Collections.reverse(blogList);//to get new one post at the top
             blogRecyclerAdapter = new BlogRecyclerAdapter(PostListActivity.this,blogList);
             recyclerView.setLayoutManager(new LinearLayoutManager(PostListActivity.this));
             recyclerView.setAdapter(blogRecyclerAdapter);
             blogRecyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}

