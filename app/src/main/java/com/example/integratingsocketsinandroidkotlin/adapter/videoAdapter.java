package com.example.integratingsocketsinandroidkotlin.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.integratingsocketsinandroidkotlin.R;
import com.example.integratingsocketsinandroidkotlin.view.activity.FullscreenActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;

public class videoAdapter extends FirebaseRecyclerAdapter<Member, videoAdapter.videoViewholder> {

    Context context;

    public videoAdapter(@NonNull FirebaseRecyclerOptions<Member> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull videoViewholder holder, int position, @NonNull Member model) {
        holder.itemName.setText(model.getName());
        holder.searchName.setText(model.getSearch());
        // holder.searchUrl.setText(model.getVideoUrl());
        // Initialize and set up ExoPlayer
        Uri videoUri = Uri.parse(model.getVideoUrl());
        MediaItem mediaItem = MediaItem.fromUri(videoUri);
        holder.exoPlayer.setMediaItem(mediaItem);
        holder.exoPlayer.prepare();
        holder.exoPlayer.setPlayWhenReady(false);
        holder.materialCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();

                Uri videoUri = Uri.parse(model.getVideoUrl());
                String videoName = model.getName();
                Intent intent = new Intent(context, FullscreenActivity.class);
                intent.putExtra("videoUri",  videoUri.toString());
                intent.putExtra("videoName", videoName.toString());
                context.startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public videoViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new videoAdapter.videoViewholder(view);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull videoViewholder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.exoPlayer.release();
    }

    class videoViewholder extends RecyclerView.ViewHolder {
        TextView itemName, searchName, searchUrl;
        PlayerView playerView;
        ExoPlayer exoPlayer;

        ShapeableImageView shapeableImageView;

        MaterialCardView materialCardView;

        public videoViewholder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.tv_item_name);
            searchName = itemView.findViewById(R.id.tv_search_name);
            //  searchUrl = itemView.findViewById(R.id.tv_search_url);
            //   playerView = itemView.findViewById(R.id.playerView_video);
            exoPlayer = new ExoPlayer.Builder(context).build();
            //  playerView.setPlayer(exoPlayer);

            //shapable Image data
            shapeableImageView = itemView.findViewById(R.id.videoImg);

            materialCardView = itemView.findViewById(R.id.materialCardView);

        }
    }
}
