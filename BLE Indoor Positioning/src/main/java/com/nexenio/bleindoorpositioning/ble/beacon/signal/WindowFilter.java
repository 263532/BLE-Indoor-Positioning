package com.nexenio.bleindoorpositioning.ble.beacon.signal;

import com.nexenio.bleindoorpositioning.ble.advertising.AdvertisingPacket;
import com.nexenio.bleindoorpositioning.ble.beacon.Beacon;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by leon on 09.01.18.
 */

public abstract class WindowFilter implements RssiFilter {

    public static long DEFAULT_DURATION = TimeUnit.SECONDS.toMillis(5);

    protected long duration = DEFAULT_DURATION;
    protected long maximumTimestamp;
    protected long minimumTimestamp;
    protected TimeUnit timeUnit;

    public WindowFilter() {
        this(DEFAULT_DURATION, TimeUnit.MILLISECONDS);
    }

    public WindowFilter(long duration, TimeUnit timeUnit) {
        this.duration = timeUnit.toMillis(duration);
        this.timeUnit = timeUnit;
    }

    public List<AdvertisingPacket> getRecentAdvertisingPackets(Beacon beacon) {
        return beacon.getAdvertisingPacketsBetween(minimumTimestamp, maximumTimestamp);
    }

    /*
        Getter & Setter
     */

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    @Override
    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration, TimeUnit timeUnit) {
        this.duration = duration;
        this.minimumTimestamp = this.maximumTimestamp - timeUnit.toMillis(duration);
    }

    public long getMaximumTimestamp() {
        return maximumTimestamp;
    }

    @Override
    public void setMaximumTimestamp(long maximumTimestamp) {
        this.maximumTimestamp = maximumTimestamp;
        this.duration = maximumTimestamp - this.minimumTimestamp;
    }

    public long getMinimumTimestamp() {
        return minimumTimestamp;
    }

    @Override
    public void setMinimumTimestamp(long minimumTimestamp) {
        this.minimumTimestamp = minimumTimestamp;
    }
}