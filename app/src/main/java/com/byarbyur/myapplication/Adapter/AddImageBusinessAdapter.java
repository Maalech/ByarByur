package com.byarbyur.myapplication.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.byarbyur.myapplication.R;
import com.byarbyur.myapplication.model.ImagePage;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;

public class AddImageBusinessAdapter extends FirestoreRecyclerAdapter<ImagePage, AddImageBusinessAdapter.ImagePageHolder> {
    
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public AddImageBusinessAdapter(@NonNull FirestoreRecyclerOptions<ImagePage> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ImagePageHolder holder, int i, @NonNull final ImagePage imagePage) {

        Picasso.get()
                .load(imagePage.getImage())
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(holder.imgProfile);




    }

    @NonNull
    @Override
    public ImagePageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item,parent,false);
        return new ImagePageHolder(v);
    }

    public void deleteItem(int position){
        getSnapshots().getSnapshot(position).getReference().delete();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount(){
        return getSnapshots().size();
    }

    class ImagePageHolder extends RecyclerView.ViewHolder{

        Button delete_btn;
        ImageView imgProfile;

        public ImagePageHolder(@NonNull final View itemView) {
            super(itemView);

            imgProfile = itemView.findViewById(R.id.image);

            delete_btn = itemView.findViewById(R.id.delete);

            delete_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    deleteItem(position);

                }
            });

        }
    }


}

