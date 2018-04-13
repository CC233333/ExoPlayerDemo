package com.master.exoplayerdemo;

import android.net.Uri;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.dash.DashChunkSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.DefaultSsChunkSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsChunkSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;

public final class ExoMediaSource {

    private ExoMediaSource() {
    }

    public static MediaSource buildMediaSource(Uri manifestUri) {
        int type = Util.inferContentType(manifestUri);
        switch (type) {
            case C.TYPE_DASH:
                return newDashMediaSource(manifestUri);
            case C.TYPE_SS:
                return newSsMediaSource(manifestUri);
            case C.TYPE_HLS:
                return newHlsMediaSource(manifestUri);
            case C.TYPE_OTHER:
                return newOtherMediaSource(manifestUri);
            default: {
                throw new IllegalStateException("Unsupported type: " + type);
            }
        }
    }

    private static MediaSource newDashMediaSource(Uri manifestUri) {
        DataSource.Factory factory = buildDataSourceFactory(new DefaultBandwidthMeter());
        DashChunkSource.Factory factory1 = new DefaultDashChunkSource.Factory(factory);
        DataSource.Factory factory2 = buildDataSourceFactory(null);
        return new DashMediaSource.Factory(factory1, factory2)
                .createMediaSource(manifestUri, null, null);
    }

    private static MediaSource newSsMediaSource(Uri manifestUri) {
        DataSource.Factory factory = buildDataSourceFactory(new DefaultBandwidthMeter());
        SsChunkSource.Factory factory1 = new DefaultSsChunkSource.Factory(factory);
        DataSource.Factory factory2 = buildDataSourceFactory(null);
        return new SsMediaSource.Factory(factory1, factory2)
                .createMediaSource(manifestUri, null, null);
    }

    private static MediaSource newHlsMediaSource(Uri manifestUri) {
        DataSource.Factory factory = buildDataSourceFactory(new DefaultBandwidthMeter());
        return new HlsMediaSource.Factory(factory)
                .createMediaSource(manifestUri, null, null);
    }

    private static MediaSource newOtherMediaSource(Uri manifestUri) {
        DataSource.Factory factory = buildDataSourceFactory(new DefaultBandwidthMeter());
        return new ExtractorMediaSource.Factory(factory)
                .createMediaSource(manifestUri, null, null);
    }

    private static DataSource.Factory buildDataSourceFactory(
            TransferListener<? super DataSource> listener) {
        return new DefaultDataSourceFactory(
                App.instance(),
                listener,
                buildHttpDataSourceFactory(listener));
    }

    private static HttpDataSource.Factory buildHttpDataSourceFactory(
            TransferListener<? super DataSource> listener) {
        return new DefaultHttpDataSourceFactory("User-Agent", listener);
    }

}
