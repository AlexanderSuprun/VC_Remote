package com.vcremote;

import android.companion.WifiDeviceFilter;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiNetworkSpecifier;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import kotlin.reflect.KParameter;


public class DevicesFragment extends Fragment {
    private ListView listView;
    private ArrayList<String> availableNetworks = new ArrayList<>();
    private Map<String, String> connectionData= new HashMap<>();
    Bundle bundle = new Bundle();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_devices, container, false);
    }
    //TODO rework to scan network
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        connectionData.put("ESP32-Access-Point", "123456789");
        if (connectionData != null) {
            for (Map.Entry<String, String> entry : connectionData.entrySet()) {
                if (connectToDeviceUsingWifi(entry.getKey(), entry.getValue())) { // check if it's able to connect
                    availableNetworks.add(entry.getKey());
                    WifiManager wifiManager = (WifiManager) Objects.requireNonNull(getActivity())
                            .getApplicationContext()
                            .getSystemService(Context.WIFI_SERVICE);
                    wifiManager.disconnect();
                }
            }
        }
        listView = Objects.requireNonNull(getView()).findViewById(R.id.fragment_devices_listview);
        Adapter adapter = new Adapter(getContext(), availableNetworks.toArray(new String[0]));
        listView.setEmptyView(getView().findViewById(R.id.fragment_devices_empty_list_message));
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getSelectedItem().toString();
                //Log.d("Selected item", parent.getSelectedItem().toString()); // return null
                connectToDeviceUsingWifi(selectedItem, connectionData.get(selectedItem));
            }
        });
    }

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
//                    @Override
//                    public void onAvailable(@NonNull Network network) {
//                        //Use this network object to Send request.
//                        //eg - Using OkHttp library to create a service request
//
//                        super.onAvailable(network);
//                    }
//                });
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
//            List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
//            for (WifiConfiguration i : list) {
//                if (i.SSID != null && i.SSID.equals("\"" + networkSSID + "\"")) {
//                    wifiManager.disconnect();
//                    result = wifiManager.enableNetwork(i.networkId, true);
//                    break;
//                }
//            }
        }
        return result;
    }
}
