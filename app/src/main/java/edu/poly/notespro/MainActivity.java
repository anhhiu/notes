package edu.poly.notespro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.Query;

import java.util.Queue;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton btnADD;
    RecyclerView recyclerView;

    ImageButton btnMenu;
    NoteAdapter noteAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnADD = (FloatingActionButton) findViewById(R.id.btnAdd);
        recyclerView = (RecyclerView) findViewById(R.id.reccylerView);
        btnMenu = (ImageButton) findViewById(R.id.btnMenunote);

        btnADD.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, NotedetailsActivity.class)));

        btnMenu.setOnClickListener(v -> showMenu() );
        setUpRecycView();
    }

    private void showMenu() {
        PopupMenu popupMenu = new PopupMenu(MainActivity.this, btnMenu );
        popupMenu.getMenu().add("Logout");
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getTitle() == "Logout"){
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                    return true;
                }
                return false;
            }
        });

    }

    void setUpRecycView(){
        Query query = Utility.getCollectionReferenceForNotes().orderBy("timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Note> options = new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query,Note.class).build();
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        noteAdapter = new NoteAdapter(options, MainActivity.this);
        recyclerView.setAdapter(noteAdapter);
    }


    @Override
    protected void onStart() {
        super.onStart();
        noteAdapter.startListening();
    }


    @Override
    protected void onStop() {
        super.onStop();
        noteAdapter.startListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        noteAdapter.notifyDataSetChanged();
    }
}
