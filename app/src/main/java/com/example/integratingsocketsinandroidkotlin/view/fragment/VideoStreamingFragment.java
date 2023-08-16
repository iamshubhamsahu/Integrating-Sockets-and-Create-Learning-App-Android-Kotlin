package com.example.integratingsocketsinandroidkotlin.view.fragment;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.integratingsocketsinandroidkotlin.R;
import com.example.integratingsocketsinandroidkotlin.adapter.Member;
import com.example.integratingsocketsinandroidkotlin.view.activity.ShowVideoActivity;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class VideoStreamingFragment extends Fragment {

    private static final int PICK_VIDEO = 1;
    private static final int RESULT_OK = -1;
    VideoView videoView;
    EditText videoName;
    TextView chooseVideo, showVideo;
    Button uploadButton;
    ProgressBar progressBar;
    private Uri videoUri;
    MediaController mediaController;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    Member member;
    UploadTask uploadTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_streaming, container, false);


        videoView = view.findViewById(R.id.videoview_main);
        videoName = view.findViewById(R.id.et_video_name);
        chooseVideo = view.findViewById(R.id.tv_choose_video);
        showVideo = view.findViewById(R.id.tv_show_video);
        uploadButton = view.findViewById(R.id.btn_upload);
        progressBar = view.findViewById(R.id.progressBar_main);

        member = new Member();
        storageReference = FirebaseStorage.getInstance().getReference("Video");
        databaseReference = FirebaseDatabase.getInstance().getReference("Video");

        mediaController = new MediaController(getActivity());
        videoView.setMediaController(mediaController);
        videoView.start();

        functionalityAddInFragment();

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_VIDEO || resultCode == RESULT_OK || data != null || data.getData() != null) {
            videoUri = data.getData();
            videoView.setVideoURI(videoUri);
        }

    }

    private void functionalityAddInFragment() {

        chooseVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_VIDEO);
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = videoName.getText().toString();
                String search = videoName.getText().toString().toLowerCase();
                if (videoUri != null || !TextUtils.isEmpty(name)) {
                    progressBar.setVisibility(View.VISIBLE);
                    final StorageReference reference = storageReference.child(System.currentTimeMillis() + "." + getExt(getContext(), videoUri));
                    uploadTask = reference.putFile(videoUri);

                    Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            return reference.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUrl = task.getResult();
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(getContext(), "Data Saved....", Toast.LENGTH_SHORT).show();

                                member.setName(name);
                                member.setVideoUrl(downloadUrl.toString());
                                member.setSearch(search);
                                String i = databaseReference.push().getKey();
                                databaseReference.child(i).setValue(member);

                                clearField();
                            } else {
                                Toast.makeText(getContext(), "Failed....", Toast.LENGTH_SHORT).show();
                            }
                        }

                        private void clearField() {
                            videoView.setVisibility(View.GONE);
                            videoView.setVisibility(View.VISIBLE);
                            videoName.setText("");

                        }
                    });
                } else {
                    Toast.makeText(getContext(), "All Fields are Required", Toast.LENGTH_SHORT).show();
                }
            }

            private String getExt(Context context, Uri videoUri) {
                ContentResolver contentResolver = context.getContentResolver();
                MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
                return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(videoUri));
            }
        });

        showVideo.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity() , ShowVideoActivity.class);
            startActivity(intent);
        });
    }
}