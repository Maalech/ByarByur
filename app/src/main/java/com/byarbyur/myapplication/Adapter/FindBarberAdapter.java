package com.byarbyur.myapplication.Adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.byarbyur.myapplication.R;
import com.byarbyur.myapplication.model.Seller;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class FindBarberAdapter extends  FirestoreRecyclerAdapter<Seller, FindBarberAdapter.BarberHolder>{

    private OnItemClickListener listener;
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public FindBarberAdapter(@NonNull FirestoreRecyclerOptions<Seller> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final BarberHolder holder, int i, @NonNull final Seller seller) {

        holder.namaTv.setText(seller.getNama());
        holder.alamatTv.setText(seller.getAlamat());
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("Barber");
        final StorageReference Ref = storageReference.child(seller.getNama()  + "/Profile.jpg");
        Ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().placeholder(R.mipmap.ic_launcher)
                        .fit().centerCrop().into(holder.imgProfile);

            }
        });



    }

    @NonNull
    @Override
    public BarberHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_barber,parent,false);
        return new BarberHolder(v);
    }


    @Override
    public int getItemCount(){
        return getSnapshots().size();
    }

    class BarberHolder extends RecyclerView.ViewHolder{

        TextView namaTv, alamatTv;
        ImageView imgProfile;

        public BarberHolder(@NonNull View itemView) {
            super(itemView);
            namaTv = itemView.findViewById(R.id.nama_usaha);
            alamatTv = itemView.findViewById(R.id.alamat_usaha);
            imgProfile = itemView.findViewById(R.id.profileImageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null){
                        listener.onItemClickListener(getSnapshots().getSnapshot(position),position);
                    }
                }
            });
        }
    }
    public interface OnItemClickListener{
        void onItemClickListener(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnClickListener(OnItemClickListener listener){
        this.listener = listener;
    }


}
