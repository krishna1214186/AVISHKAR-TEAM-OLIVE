package com.krishna.team_olive.AddingItem;

import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.krishna.team_olive.AddingItem.AddedItemDetailFilling_2;
import com.krishna.team_olive.R;

public class FragmentFileUpload extends BottomSheetDialogFragment {

    ImageView iv_camera_upload, iv_video_upload, iv_gallery_upload;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_file_upload, container, false);

        iv_camera_upload = view.findViewById(R.id.iv_camera_upload);
        iv_gallery_upload = view.findViewById(R.id.iv_gallery_upload);
        iv_video_upload = view.findViewById(R.id.iv_video_upload);

        iv_gallery_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddedItemDetailFilling_2.class);
                intent.putExtra("click","clicked_gallery");
                getActivity().startActivity(intent);
            }
        });

        iv_camera_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddedItemDetailFilling_2.class);
                intent.putExtra("click","clicked_camera");
                getActivity().startActivity(intent);
            }
        });

        iv_video_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddedItemDetailFilling_2.class);
                intent.putExtra("click","clicked_video");
                getActivity().startActivity(intent);
            }
        });


        return view;
    }

}