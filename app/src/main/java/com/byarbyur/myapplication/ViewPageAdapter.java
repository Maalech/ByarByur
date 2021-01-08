package com.byarbyur.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ViewPageAdapter extends PagerAdapter {

    Context context;
    List<ImagePage> imagePageList;
    LayoutInflater inflater;

    public ViewPageAdapter(Context context, List<ImagePage> imagePageList) {
        this.context = context;
        this.imagePageList = imagePageList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return imagePageList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ((ViewPager)container).removeView((View)object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        //inflate
        View view = inflater.inflate(R.layout.view_pager_item,container,false);

        //view
        ImageView seller_image = view.findViewById(R.id.img_page);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"Bisa diklik", Toast.LENGTH_SHORT).show();
            }
        });

        Picasso.get().load(imagePageList.get(position).getImage()).into(seller_image);

        container.addView(view);
        return view;
    }
}
