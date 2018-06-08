package itch.ath.com.itchyscratchy.serviceexample;

import android.content.Context;
import android.util.Log;

public class MyServiceDelegate26 extends MyServiceDelegate21 {

    @Override
    protected boolean onStartJob( Context context, MyServiceParams params ) {
        Log.d( "ATH", "onStart: Some additional behaviors for SDK 26" );
        return super.onStartJob( context, params );
    }

    @Override
    protected boolean onStopJob( Context context, MyServiceParams params ) {
        Log.d( "ATH", "onStop: Some additional behaviors for SDK 26" );
        return super.onStopJob( context, params );
    }
   
}
