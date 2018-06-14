package itch.ath.com.itchyscratchy.commonstuff;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;

import itch.ath.com.itchyscratchy.serviceexample.MyJobService;

public class ApiHelper {

    /**
     * Return the best {@link TargetApiImpl} for the given defImplClass
     */
    public static <T> T bestInstance( Class<T> defImplClass ) {
        TargetApiImpls impls = defImplClass.getAnnotation( TargetApiImpls.class );
        Class<?> bestClass = defImplClass;
        int bestSdk = 0;
        if ( impls != null ) {
            for ( TargetApiImpl impl : impls.value() ) {
                if ( Build.VERSION.SDK_INT >= impl.sdk() && Build.VERSION.SDK_INT > bestSdk ) {
                    bestSdk = impl.sdk();
                    bestClass = impl.impl();
                }
            }
        }

        try {
            return (T) bestClass.newInstance();
        } catch ( Exception e ) {
            // Should never get past developer tests -- misuse -- just blow up
            throw new IllegalStateException( "Unable to construct " + bestClass + " make sure you have an empty constructor", e );
        }
    }

    /**
     * Note in the case of SDK 21+ any repeat interval less than 15 minutes will be set to 15 minutes
     *
     * @param serviceClass the service class to be started. JobService or JobIntentService for 21+
     * @param params       anything extending JobParamsVO
     * @param interval     how often should the job repeat
     */
    public static <T extends JobParamsVO> void startRepeating( Context context, Class<? extends Service> serviceClass, T params, long interval ) {
        if ( Build.VERSION.SDK_INT < 21 ) {
            startRepeating16( context, serviceClass, params, interval );
        } else {
            startRepeating21( context, serviceClass, params, interval );
        }
    }

    @RequiresApi( api = Build.VERSION_CODES.JELLY_BEAN )
    private static <T extends JobParamsVO> void startRepeating16( Context context, Class<? extends Service> serviceClass, T params, long interval ) {
        Intent intent = new Intent( context, serviceClass );
        JobParams.toIntent( params, intent );
        PendingIntent pintent = PendingIntent.getService( context, 0, intent, 0 );
        AlarmManager alarm = (AlarmManager) context.getSystemService( Context.ALARM_SERVICE );
        long triggerAtMills = SystemClock.elapsedRealtime();
        alarm.setRepeating( AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtMills, interval, pintent );
    }

    @RequiresApi( api = Build.VERSION_CODES.LOLLIPOP )
    private static <T extends JobParamsVO> void startRepeating21( Context context, Class<? extends Service> serviceClass, T params, long interval ) {
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService( Context.JOB_SCHEDULER_SERVICE );
        JobInfo info = new JobInfo.Builder( MyJobService.JOB_ID, new ComponentName( context, serviceClass ) )
                .setRequiredNetworkType( JobInfo.NETWORK_TYPE_ANY )
                .setPeriodic( interval ) // 15 minute minimum on SDK 24+
                .build();
        JobParams.toJobInfo( params, info );
        jobScheduler.schedule( info );
    }
}
