package itch.ath.com.itchyscratchy.serviceexample;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import itch.ath.com.itchyscratchy.commonstuff.ApiHelper;

public class MyServiceDelegate16 extends MyService {
    public void startRepeating( Context context, MyServiceParams params ) {
        ApiHelper.startRepeating( context, MyTraditionalService.class, params, 5000 );
    }

    /**
     * @return true if the operation completed successfully
     */
    protected boolean onStartJob( Context context, MyServiceParams params ) {
        String message = "onStart: " + params.getMessage() + " " + params.getSomeValue();
        Toast.makeText( context, message, Toast.LENGTH_LONG ).show();
        Log.d( "ATH", message );
        return true;
    }

    /**
     * @return true if the operation completed successfully
     */
    protected boolean onStopJob( Context context, MyServiceParams params ) {
        String message = "onStop: " + params.getMessage() + " " + params.getSomeValue();
        Toast.makeText( context, message, Toast.LENGTH_LONG ).show();
        Log.d( "ATH", message );
        return true;
    }
}
