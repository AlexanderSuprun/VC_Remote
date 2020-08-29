package com.vcremote.fragment;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.vcremote.R;

import java.io.IOException;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HomeFragment extends Fragment {
    private final String SSID = "ESP8266-Access-Point";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);

    }

    /**
     * Controls turn on button
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final Button btnPower = (Button) Objects.requireNonNull(getView()).findViewById(R.id.fragment_home_turn_on_btn);
        WifiManager wifiManager = (WifiManager) Objects.requireNonNull(getActivity())
                .getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null && wifiManager.getConnectionInfo() != null) {
                btnPower.setEnabled(true);
                btnPower.setTag("turnOn");
                btnPower.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String status = (String) v.getTag();
                        if (status.equals("turnOn")) {
                            try {
                                sendHttpRequest(true);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            btnPower.setText(R.string.turn_off_btn);
                            btnPower.setBackgroundResource(R.drawable.bg_turn_off_btn);
                            v.setTag("turnOff");
                        } else {
                            try {
                                sendHttpRequest(false);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            btnPower.setText(R.string.turn_on_btn);
                            btnPower.setBackgroundResource(R.drawable.bg_turn_on_btn);
                            btnPower.setTag("turnOn");
                        }
                    }
                });
        }
    }

    /**
     * Sends HTTP request to server
     * @param enableDevice set true to enable device or false to disable
     * @throws IOException if the request could not be executed due to cancellation, a connectivity
     *      problem or timeout. Because networks can fail during an exchange, it is possible that the
     *      remote server accepted the request before the failure.
     */
    private void sendHttpRequest(boolean enableDevice) throws IOException {
        final String TURN_ON_URL = "http:/192.168.1.1/turn_on";
        final String TURN_OFF_URL = "http:/192.168.1.1/turn_off";
        OkHttpClient client = new OkHttpClient();

        if (enableDevice) {
            Request request = new Request.Builder()
                    .url(TURN_ON_URL)
                    .build();
            client.newCall(request).execute();
        } else {
            Request request = new Request.Builder()
            .url(TURN_OFF_URL)
            .build();
            client.newCall(request).execute();
        }
    }
}
