package edu.poly.notespro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

public class NotedetailsActivity extends AppCompatActivity {
    EditText edttitle,edtcontent;
    ImageButton btnsave;
    TextView tvpageTitle;
    String title, content, docId;
    boolean isEditMode = false;
    TextView tvdelNote;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notedetails);

        edttitle = (EditText) findViewById(R.id.edtNotestitle);
        edtcontent = (EditText) findViewById(R.id.edtNotesContent);
        btnsave = (ImageButton) findViewById(R.id.btnSaveNote);
        tvpageTitle = (TextView) findViewById(R.id.tvPageTitle);
        tvdelNote = (TextView)findViewById(R.id.tvdel);

        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("context");
        docId = getIntent().getStringExtra("docId");

        if (docId!= null && !docId.isEmpty()){
            isEditMode = true;
        }
        edttitle.setText(title);
        edtcontent.setText(content);

        if (isEditMode){
            tvpageTitle.setText("Edit your note");
            tvdelNote.setVisibility(View.VISIBLE);
        }

        btnsave.setOnClickListener(v -> saveNote());

        tvdelNote.setOnClickListener(v -> delNotes());

    }

     void delNotes() {

        DocumentReference documentReference;
        documentReference = Utility.getCollectionReferenceForNotes().document(docId);
        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Utility.showToast(NotedetailsActivity.this, "Note deleted successfully");
                    finish();
                }
                else{
                    Utility.showToast(NotedetailsActivity.this, "Faied while deleting note");

                }
            }
        });

    }

    private void saveNote() {
        String title = edttitle.getText().toString();
        String content = edtcontent.getText().toString();
        if (title == null || title.isEmpty()){
            edttitle.setError("Title is required");
            return;
        }
        Note note = new Note();
        note.setTitle(title);
        note.setContent(content);
        note.setTimestamp(Timestamp.now());
        saveNoteToFirebase(note);
    }

    void saveNoteToFirebase(Note note){
        DocumentReference documentReference;

        if (isEditMode){
            //update the note
            documentReference = Utility.getCollectionReferenceForNotes().document(docId);
        }else {
            //create new note
            documentReference = Utility.getCollectionReferenceForNotes().document();
        }

        documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Utility.showToast(NotedetailsActivity.this, "Note added successfully");
                    finish();
                }
                else{
                    Utility.showToast(NotedetailsActivity.this, "Faied while adding note");

                }
            }
        });
    }
}