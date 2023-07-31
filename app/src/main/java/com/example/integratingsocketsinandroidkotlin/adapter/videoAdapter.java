package com.example.integratingsocketsinandroidkotlin.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.integratingsocketsinandroidkotlin.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.*;

public class videoAdapter extends FirebaseRecyclerAdapter<Member, videoAdapter.videoViewholder> {

    Context context;
    ExoPlayer  exoPlayer;

    public videoAdapter(@NonNull FirebaseRecyclerOptions<Member> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull videoViewholder holder, int position, @NonNull Member model) {
        holder.itemName.setText(model.getName());
        holder.searchName.setText(model.getSearch());
        holder.searchUrl.setText(model.getVideoUrl());
        // Initialize and set up ExoPlayer
        Uri videoUri = Uri.parse(model.getVideoUrl());
        MediaItem mediaItem = MediaItem.fromUri(videoUri);
        holder.exoPlayer.setMediaItem(mediaItem);
        holder.exoPlayer.prepare();
        holder.exoPlayer.setPlayWhenReady(false);

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
        public videoViewholder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.tv_item_name);
            searchName = itemView.findViewById(R.id.tv_search_name);
            searchUrl = itemView.findViewById(R.id.tv_search_url);
            playerView = itemView.findViewById(R.id.playerView_video);
            exoPlayer = new ExoPlayer.Builder(context).build();
            playerView.setPlayer(exoPlayer);
        }
    }



}
