package itch.ath.com.itchyscratchy.intentserviceexample;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.util.Log;

import itch.ath.com.itchyscratchy.R;
import itch.ath.com.itchyscratchy.commonstuff.ApiHelper;
import itch.ath.com.itchyscratchy.commonstuff.JobParams;
import itch.ath.com.itchyscratchy.commonstuff.JobParamsVO;

/**
 * JobIntentService is much simpler than JobService because JobIntentService is backwards compatible
 * so we don't need abstractions to handle various SDK versions. However, we do still need to start
 * the service appropriately based on the SDK version.
 */
public class MyJobIntentService extends JobIntentService {
    public static final int JOB_ID = R.id.MyJobIntentService;

    public static void start( Context context, MyJobIntentServiceParams params ) {
        Intent work = new Intent();
        JobParams.toIntent( params, work );
        enqueueWork( context, MyJobIntentService.class, JOB_ID, work );
    }

    public static void startRepeating( Context context, MyJobIntentServiceParams params ) {
        ApiHelper.startRepeating( context, MyJobIntentService.class, params, 5000 );
    }

    @Override
    protected void onHandleWork( @NonNull Intent intent ) {
        MyJobIntentServiceParams params = JobParams.fromIntent( intent, MyJobIntentServiceParams.class );
        Log.d( "ATH", String.format( "MyJobIntentService.onHandleWork: action=%s, message=%s", params.getMessage(), params.getSomeValue() ) );
    }

    // May get called before the work is completed in case of long processes or limited resources
    @Override
    public boolean onStopCurrentWork() {
        Log.d( "ATH", "MyJobIntentService.onStopCurrentWork" );

        // return true to indicate work was not completed and this task should be rescheduled
        // return false to indicate the work was completed
        return super.onStopCurrentWork();
    }

    // Simple data object to carry our params between implementations
    // A builder could harden the contract for what is required to operate the service
    // FYI -- This is the most critical part as passing data
    // to a service has changed and this serves to decouple that change
    // Specifically
    // - Service use Bundles via Intent
    // - JobService use PersistableBundle via JobInfo
    public static class MyJobIntentServiceParams extends JobParamsVO {

        /* Required for Bundle and PersistedBundle conversion */
        protected MyJobIntentServiceParams( JobParams params ) {
            super( params );
        }

        /* Per your own requirements */
        public MyJobIntentServiceParams( String message, String someValue ) {
            super();
            // Values are stored internally as a Bundle for compatibility
            params().putString( "message", message );
            params().putString( "someValue", someValue );
        }

        public String getMessage() {
            return params().getString( "message" );
        }

        public String getSomeValue() {
            return params().getString( "someValue" );
        }
    }
}
