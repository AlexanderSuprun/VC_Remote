package com.vcremote;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final Button powerBtn = (Button) getView().findViewById(R.id.turn_on_btn);
        powerBtn.setTag(1);

        powerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int status = (int) v.getTag();
                if(status == 1) {
                    powerBtn.setText(R.string.turnOff_btn);
                    powerBtn.setBackgroundResource(R.drawable.bg_turn_off_btn);
                    v.setTag(0);
                } else {
                    powerBtn.setText(R.string.turnOn_btn);
                    powerBtn.setBackgroundResource(R.drawable.bg_turn_on_btn);
                    powerBtn.setTag(1);
                }
            }
        });
    }
}
