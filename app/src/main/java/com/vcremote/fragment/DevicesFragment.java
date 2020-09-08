package com.vcremote.fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiNetworkSpecifier;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.vcremote.R;
import com.vcremote.base.BaseFragment;
import com.vcremote.utils.adapter.Adapter;

import java.util.Objects;


public class DevicesFragment extends BaseFragment {
    private ListView listView;
    private String ssid = "ESP8266-Access-Point";
    private String password = "PEGw5QivntQfhu";

    private Bundle bundle = new Bundle();
    private Fragment HomeFragment = new HomeFragment();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_devices, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String[] networksList = {ssid};
        listView = Objects.requireNonNull(getView()).findViewById(R.id.fragment_devices_listview);
        Adapter adapter = new Adapter(getContext(), networksList);
        listView.setEmptyView(getView().findViewById(R.id.fragment_devices_empty_list_message));
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!view.isSelected()) {
                    if (connectToDeviceUsingWifi(ssid, password)) {
                        view.setSelected(true);
//                        HomeFragment.setArguments(bundle);
//                        getFragmentManager()
//                                .beginTransaction()
//                                .replace()
//                                .commit();
                    }
                }
            }
        });
    }

    /**
     * Connects to device using WiFi network
     * @param networkSSID SSID for WiFi network
     * @param networkPassword password for WiFi network
     * @return true if connected successfully
     */
    private boolean connectToDeviceUsingWifi(String networkSSID, String networkPassword) {
        boolean result = false;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

            WifiNetworkSpecifier.Builder builder = new WifiNetworkSpecifier.Builder();
            builder.setSsid(networkSSID);
            builder.setWpa2Passphrase(networkPassword);
            WifiNetworkSpecifier wifiNetworkSpecifier = builder.build();

            NetworkRequest.Builder networkRequestBuilder = new NetworkRequest.Builder();
            networkRequestBuilder.addTransportType(NetworkCapabilities.TRANSPORT_WIFI);
            networkRequestBuilder.addCapability(NetworkCapabilities.NET_CAPABILITY_NOT_RESTRICTED);
            networkRequestBuilder.addCapability(NetworkCapabilities.NET_CAPABILITY_TRUSTED);
            networkRequestBuilder.setNetworkSpecifier(wifiNetworkSpecifier);
            NetworkRequest networkRequest = networkRequestBuilder.build();
            ConnectivityManager cm = (ConnectivityManager) Objects.requireNonNull(getActivity())
                    .getApplicationContext()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm != null) {
                result = true;
                cm.requestNetwork(networkRequest, new ConnectivityManager.NetworkCallback());
            }
        } else {
            WifiManager wifiManager = (WifiManager) Objects.requireNonNull(getActivity())
                    .getApplicationContext()
                    .getSystemService(Context.WIFI_SERVICE);
            if (!Objects.requireNonNull(wifiManager).isWifiEnabled()) {
                wifiManager.setWifiEnabled(true);
            }

            WifiConfiguration config = new WifiConfiguration();
            config.SSID = "\"" + networkSSID + "\"";
            config.preSharedKey = "\"" + networkPassword + "\"";
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            int networkID = wifiManager.addNetwork(config);
            Log.d("Network SSID", networkSSID);
            Log.d("Network pass", networkPassword);
            wifiManager.disconnect();
            result = wifiManager.enableNetwork(networkID, true);
            Log.d("Result = wifiManager", String.valueOf(result));
        }
        return result;
    }
}
