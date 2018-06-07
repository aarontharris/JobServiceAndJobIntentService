package itch.ath.com.itchyscratchy.serviceexample;

import android.content.Context;

import itch.ath.com.itchyscratchy.commonstuff.JobParams;
import itch.ath.com.itchyscratchy.commonstuff.JobParamsVO;
import itch.ath.com.itchyscratchy.commonstuff.TargetApiImpl;
import itch.ath.com.itchyscratchy.commonstuff.TargetApiImpls;

import static itch.ath.com.itchyscratchy.commonstuff.ApiHelper.bestInstance;

/**
 * This is unfortunately much more complex than it should be, but also the best pattern for
 * compatibility and funneling logic through a centralized place
 * <p>
 * <pre>
 * In this example, {@link MyService} is the point of contact for consumers.
 * An activity might do this:
 *   MyService.get().startRepeating(this, ...)
 *
 * MyService just defines the mapping to delegate services and the required data to start the service.
 * Delegates are mapped as being able to handle any SDK less-than-or-equal-to the targeted, see the annotations below.
 * Each delegate defines how the service should be started for its SDK and how the work should be performed.
 * Additional delegates can override the behavior as necessary.
 *
 * This is all easy, however the complicated part is the flow, which is as follows:
 * 1. MyService.get().startRepeating(this, ...)
 *    #1 reroutes the startRepeating call to the appropriate delegate,
 *       this delegate describes how the service should be started for that SDK.
 *       For this example lets pretend the SDK of the device is 21
 * 2. {@link MyServiceDelegate21} then starts the service appropriately for its SDK
 *    #2 SDK21+ uses JobScheduler, so once JobScheduler starts the command, {@link MyJobService} is called.
 *       {@link MyJobService} is just a basic wrapper to intercept the service calls and delegate
 * 3. {@link MyJobService} now delegates the work back to {@link MyServiceDelegate21} to perform the operations.
 *    #3 Unfortunately the instance of {@link MyServiceDelegate21} used in Step2 is not the same instance used in
 *       Step3 and Step4
 * 4. {@link MyJobService} *may* get a call to stop which is also delegated to {@link MyServiceDelegate21} and is
 *     the same instance as used in Step3
 *
 * The reason Step2 has a different instance than Step3 and Step4 is that there may be seconds, hours, weeks
 * between Step2 and Step3, while Step3 and Step4 should only be moments.
 *
 * Note: In Step1, if the device SDK was less than 21, {@link MyServiceDelegate16} would have been used to start the
 * service, in which case {@link MyTraditionalService} would have started and delegated instead of {@link MyJobService}
 *
 * Another interesting point -- Why did I put the SDK16 implementation into it's own Class instead of just placing
 * the base implementation in this Class? You can do it either way; however, by placing the SDK16 work into it's own
 * class, its easy to just remove that class when the minSdk becomes 21 in the future. If I bake it into this class
 * I have to bother with copying and pasting and rewiring. As a bonus, this Class now reads as a simple interface.
 * </pre>
 */

// Define the mapping of SDK => Implementation
// Tells the automagic part how to find the best implementation
@TargetApiImpls( {
        @TargetApiImpl( sdk = 16, impl = MyServiceDelegate16.class ),
        @TargetApiImpl( sdk = 21, impl = MyServiceDelegate21.class ),
        @TargetApiImpl( sdk = 26, impl = MyServiceDelegate26.class ),
} )
public class MyService {

    /**
     * Get the best implementation delegate for your SDK. Returns a new instance each time, best to
     * locally cache this.
     */
    public static MyService get() {
        return bestInstance( MyService.class );
    }

  /*
   * FYI - Beyond this point, this code is boilerplate and can be placed into a base class
   */

    /**
     * Warning -- don't do anything here other than start your service appropriate to the SDK you've
     * chosen to support. The instance used to call this method is not the same instance used to later
     * call {@link #onStartJob(Context, MyServiceParams)} and {@link #onStopJob(Context,
     * MyServiceParams)}
     */
    public void startRepeating( Context context, MyServiceParams params ) {
    }

    /**
     * @return true if the operation completed successfully
     */
    protected boolean onStartJob( Context context, MyServiceParams params ) {
        return true;
    }

    /**
     * @return true if the operation completed successfully
     */
    protected boolean onStopJob( Context context, MyServiceParams params ) {
        return true;
    }

    // Simple data object to carry our params between implementations
    // A builder could harden the contract for what is required to operate the service
    // FYI -- This is the most critical part as passing data
    // to a service has changed and this serves to decouple that change
    // Specifically
    // - Service use Bundles via Intent
    // - JobService use PersistableBundle via JobInfo
    public static class MyServiceParams extends JobParamsVO {

        /* Required for Bundle and PersistedBundle conversion */
        protected MyServiceParams( JobParams params ) {
            super( params );
        }

        /* Per your own requirements */
        public MyServiceParams( String message, long someValue ) {
            super();
            // Values are stored internally as a Bundle for compatibility
            params().putString( "message", message );
            params().putLong( "someValue", someValue );
        }

        public String getMessage() {
            return params().getString( "message" );
        }

        public long getSomeValue() {
            return params().getLong( "someValue" );
        }
    }
}
