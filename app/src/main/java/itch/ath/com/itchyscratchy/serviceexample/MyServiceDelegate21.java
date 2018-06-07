package itch.ath.com.itchyscratchy.serviceexample;

import android.content.Context;
import android.util.Log;

import itch.ath.com.itchyscratchy.commonstuff.ApiHelper;

public class MyServiceDelegate21 extends MyServiceDelegate16 {

    @Override
    public void startRepeating( Context context, MyServiceParams params ) {
        ApiHelper.startRepeating( context, MyJobService.class, params, 5000 );
    }

    @Override
    protected boolean onStartJob( Context context, MyServiceParams params ) {
        Log.d( "ATH", "onStart: Some additional behaviors for SDK 21" );
        return super.onStartJob( context, params );
    }

    @Override
    protected boolean onStopJob( Context context, MyServiceParams params ) {
        Log.d( "ATH", "onStop: Some additional behaviors for SDK 21" );
        return super.onStopJob( context, params );
    }
}
