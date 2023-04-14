package edu.poly.notespro;

import android.content.Context;
import android.content.Intent;
import android.view.ContentInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;

public class NoteAdapter extends FirestoreRecyclerAdapter<Note, NoteAdapter.NoteViewHolder> {
    Context context;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public NoteAdapter(@NonNull FirestoreRecyclerOptions<Note> options, Context content) {
        super(options);
        this.context = content;
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull Note note) {
        holder.tvtit.setText(note.title);
        holder.tvcont.setText(note.content);
        holder.tvtime.setText(Utility.timestampToString(note.timestamp));

        holder.itemView.setOnClickListener((v) -> {
            Intent intent = new Intent(context, NotedetailsActivity.class);
            intent.putExtra("title", note.title);
            intent.putExtra("context", note.content);
            String docId = this.getSnapshots().getSnapshot(position).getId();
            intent.putExtra("docId", docId);
            context.startActivity(intent);
        });
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview, parent, false);
      return new NoteViewHolder(view);
    }

    class NoteViewHolder extends RecyclerView.ViewHolder {

        TextView tvtit, tvcont, tvtime;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);

            tvtit = itemView.findViewById(R.id.tvtitlenote);
            tvcont = itemView.findViewById(R.id.tvcontentnote);
            tvtime = itemView.findViewById(R.id.tvtimetamp);



        }
    }
}
