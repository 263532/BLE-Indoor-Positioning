package com.nexenio.bleindoorpositioningdemo.ui.beaconview.chart;


import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.nexenio.bleindoorpositioning.ble.beacon.Beacon;
import com.nexenio.bleindoorpositioning.ble.beacon.BeaconUpdateListener;
import com.nexenio.bleindoorpositioning.ble.beacon.IBeacon;
import com.nexenio.bleindoorpositioning.ble.beacon.filter.IBeaconFilter;
import com.nexenio.bleindoorpositioning.location.Location;
import com.nexenio.bleindoorpositioning.location.listener.LocationListener;
import com.nexenio.bleindoorpositioning.location.provider.LocationProvider;
import com.nexenio.bleindoorpositioningdemo.R;
import com.nexenio.bleindoorpositioningdemo.ui.beaconview.BeaconViewFragment;
import com.nexenio.bleindoorpositioningdemo.ui.beaconview.ColorUtil;

import java.util.ArrayList;
import java.util.UUID;

public class BeaconChartFragment extends BeaconViewFragment {

    private BeaconChart beaconChart;

    public BeaconChartFragment() {
        super();

        IBeaconFilter uuidFilter = new IBeaconFilter() {

            private UUID legacyUuid = UUID.fromString("acfd065e-c3c0-11e3-9bbe-1a514932ac01");
            private UUID gateDetectionUuid = UUID.fromString("f175c9a8-d51c-4d25-8449-4d3d340d1067");
            private UUID indoorPositioningUuid = UUID.fromString("03253fdd-55cb-44c2-a1eb-80c8355f8291");

            //TODO filter for one beacon

            @Override
            public boolean matches(IBeacon beacon) {
                if (legacyUuid.equals(beacon.getProximityUuid())) {
                    return true;
                }
                if (indoorPositioningUuid.equals(beacon.getProximityUuid())) {
                    return true;
                }
                if (gateDetectionUuid.equals(beacon.getProximityUuid())) {
                    return true;
                }
                return false;
            }
        };
        beaconFilters.add(uuidFilter);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_beacon_chart;
    }

    @Override
    protected LocationListener createDeviceLocationListener() {
        return new LocationListener() {
            @Override
            public void onLocationUpdated(LocationProvider locationProvider, Location location) {
                // TODO: remove artificial noise
                //location.setLatitude(location.getLatitude() + Math.random() * 0.0002);
                //location.setLongitude(location.getLongitude() + Math.random() * 0.0002);
                beaconChart.setDeviceLocation(location);
            }
        };
    }

    @Override
    protected BeaconUpdateListener createBeaconUpdateListener() {
        return new BeaconUpdateListener() {
            @Override
            public void onBeaconUpdated(Beacon updatedBeacon) {
                beaconChart.setBeacons(getBeacons());
            }
        };
    }

    @CallSuper
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflatedView = super.onCreateView(inflater, container, savedInstanceState);
        beaconChart = inflatedView.findViewById(R.id.beaconChart);
        beaconChart.setBeacons(new ArrayList<>(beaconManager.getBeaconMap().values()));
        return inflatedView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.beacon_chart_view, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_value_rssi: {
                onValueTypeSelected(BeaconChart.VALUE_TYPE_RSSI, item);
                return true;
            }
            case R.id.menu_value_distance: {
                onValueTypeSelected(BeaconChart.VALUE_TYPE_DISTANCE, item);
                return true;
            }
            case R.id.menu_value_frequency: {
                onValueTypeSelected(BeaconChart.VALUE_TYPE_FREQUENCY, item);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onValueTypeSelected(@BeaconChart.ValueType int valueType, MenuItem menuItem) {
        menuItem.setChecked(true);
        beaconChart.setValueType(valueType);
    }

    @Override
    protected void onColoringModeSelected(@ColorUtil.ColoringMode int coloringMode, MenuItem menuItem) {
        super.onColoringModeSelected(coloringMode, menuItem);
        beaconChart.setColoringMode(coloringMode);
    }

}
