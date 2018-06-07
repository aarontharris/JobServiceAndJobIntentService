package itch.ath.com.itchyscratchy;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import itch.ath.com.itchyscratchy.serviceexample.MyService;

import static itch.ath.com.itchyscratchy.intentserviceexample.MyJobIntentService.MyJobIntentServiceParams;
import static itch.ath.com.itchyscratchy.intentserviceexample.MyJobIntentService.start;
import static itch.ath.com.itchyscratchy.intentserviceexample.MyJobIntentService.startRepeating;
import static itch.ath.com.itchyscratchy.serviceexample.MyServiceDelegate16.MyServiceParams;

public class LaunchActivity extends AppCompatActivity {
    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_launch );

        // IntentService and JobIntentService once
        findViewById( R.id.intent_service_button ).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                Log.d( "ATH", "onClick" );
                start( LaunchActivity.this, new MyJobIntentServiceParams( "Hello World", "some value" ) );
            }
        } );

        // IntentService and JobIntentService repeating
        findViewById( R.id.intent_service_button_repeating ).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                Log.d( "ATH", "onClick" );
                startRepeating( LaunchActivity.this, new MyJobIntentServiceParams( "Hello World", "some value" ) );
            }
        } );

        // Service and JobService repeating
        findViewById( R.id.service_button ).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                Log.d( "ATH", "onClick" );
                MyService.get().startRepeating( LaunchActivity.this, new MyServiceParams( "Hello World", 42 ) );
            }
        } );
    }
}
