package itch.ath.com.itchyscratchy.serviceexample;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import itch.ath.com.itchyscratchy.R;
import itch.ath.com.itchyscratchy.commonstuff.JobParams;
import itch.ath.com.itchyscratchy.serviceexample.MyService.MyServiceParams;

/**
 * The only purpose of MyTraditionalService is to intercept service calls for SDK < 21 and send
 * those calls to the appropriate delegate
 */
public class MyTraditionalService extends Service {
    public static final int JOB_ID = R.id.MyTraditionalService;

    private MyService delegate;
    private MyServiceParams params;

    @Nullable
    @Override
    public IBinder onBind( Intent intent ) {
        return null;
    }

    @Override
    public int onStartCommand( Intent intent, int flags, int startId ) {
        int out = super.onStartCommand( intent, flags, startId );
        delegate = MyService.get();
        params = JobParams.fromIntent( intent, MyServiceParams.class );
        delegate.onStartJob( this, params );
        return out;
    }

    @Override
    public void onDestroy() {
        delegate.onStopJob( this, params );
        super.onDestroy();
    }
}
