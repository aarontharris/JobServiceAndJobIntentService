package itch.ath.com.itchyscratchy.serviceexample;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;
import android.support.annotation.RequiresApi;

import itch.ath.com.itchyscratchy.R;
import itch.ath.com.itchyscratchy.commonstuff.JobParams;

import static itch.ath.com.itchyscratchy.serviceexample.MyServiceDelegate16.MyServiceParams;

/**
 * The only purpose of MyTraditionalService is to intercept service calls for SDK >= 21 and send
 * those calls to the appropriate delegate
 */
@RequiresApi( api = Build.VERSION_CODES.LOLLIPOP )
public class MyJobService extends JobService {
    public static final int JOB_ID = R.id.MyJobService;
    private MyService delegate;

    @Override
    public boolean onStartJob( JobParameters params ) {
        delegate = MyService.get();
        return delegate.onStartJob( this, JobParams.fromJobParameters( params, MyServiceParams.class ) );
    }

    @Override
    public boolean onStopJob( JobParameters params ) {
        return delegate.onStartJob( this, JobParams.fromJobParameters( params, MyServiceParams.class ) );
    }
}
