package com.vcremote;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiNetworkSpecifier;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

public class DevicesFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_devices, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final Button btnConnect = (Button) getView().findViewById(R.id.btn_connect);
        final TextView textviewStatus = (TextView) getView().findViewById(R.id.status);

        btnConnect.setTag("connect");

        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String status = (String) v.getTag();
                if (status.equals("connect")) {
                    if(connectToWifiAP()) {
                        btnConnect.setText(R.string.disconnect);
                        btnConnect.setBackgroundResource(R.drawable.bg_turn_off_btn);
                        btnConnect.setTag("disconnect");
                        textviewStatus.setText(R.string.connected);
                    }
                } else {
                    btnConnect.setText(R.string.connect);
                    btnConnect.setBackgroundResource(R.drawable.bg_turn_on_btn);
                    btnConnect.setTag("connect");
                    textviewStatus.setText(R.string.disconnected);
                }
            }
        });
    }

    private boolean connectToWifiAP() {
        String networkSSID = "ESP32-Access-Point";
        String networkPassword = "123456789";

        WifiConfiguration config = new WifiConfiguration();
        config.SSID = "\"" + networkSSID + "\"";
        config.preSharedKey = "\"" + networkPassword + "\"";
        config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);

        WifiManager wifiManager = (WifiManager) getContext().getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE);
        int networkID = wifiManager.addNetwork(config);
        wifiManager.disconnect();
        boolean result = wifiManager.enableNetwork(networkID, true);
        wifiManager.reconnect();
        return result;
    }
}
