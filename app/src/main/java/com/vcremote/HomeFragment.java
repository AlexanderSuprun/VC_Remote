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
        final Button btnPower = (Button) getView().findViewById(R.id.fragment_home_turn_on_btn);
        btnPower.setTag("turnOn");

        btnPower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String status = (String) v.getTag();
                if(status.equals("turnOn")) {
                    btnPower.setText(R.string.turnOff_btn);
                    btnPower.setBackgroundResource(R.drawable.bg_turn_off_btn);
                    v.setTag("turnOff");
                } else {
                    btnPower.setText(R.string.turnOn_btn);
                    btnPower.setBackgroundResource(R.drawable.bg_turn_on_btn);
                    btnPower.setTag("turnOn");
                }
            }
        });
    }
}
