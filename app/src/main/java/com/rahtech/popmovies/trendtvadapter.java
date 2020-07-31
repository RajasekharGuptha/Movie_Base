package com.rahtech.popmovies;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;


public class trendtvadapter extends RecyclerView.Adapter<trendtvadapter.viewholder> {

    final private onTvClickListener onClickListener;
    private Activity mainActivity;
    private ArrayList<details> detailsArrayList;

    public interface onTvClickListener {
        void onTvClick(int clickInt);
    }


    public trendtvadapter(Activity mainActivity, ArrayList<details> detailsArrayList, onTvClickListener itemClickListener) {
        this.mainActivity = mainActivity;
        this.detailsArrayList = detailsArrayList;
        this.onClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        View view = layoutInflater.inflate(R.layout.poster_layout, parent, false);

        return new viewholder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final viewholder holder, int position) {
        String uri = "https://image.tmdb.org/t/p/w185/" + detailsArrayList.get(position).getPoster_path();

        Picasso.Builder picassoBuilder = new Picasso.Builder(mainActivity);
        picassoBuilder.listener(new Picasso.Listener() {
            @Override
            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                Log.e(TAG, "onImageLoadFailed: " + exception);
            }
        });
        picassoBuilder.build().load(uri).resize(125, 200).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return detailsArrayList.size();
    }


    class viewholder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        CardView cardView;
        TextView textView;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            this.cardView = itemView.findViewById(R.id.posterParentCard);
        this.imageView=itemView.findViewById(R.id.poster);
//            this.textView = itemView.findViewById(R.id.title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onClickListener.onTvClick(getAdapterPosition());
        }
    }
}