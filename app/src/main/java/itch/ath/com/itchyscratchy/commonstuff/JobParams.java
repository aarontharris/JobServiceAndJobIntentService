package itch.ath.com.itchyscratchy.commonstuff;

import android.annotation.TargetApi;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.lang.reflect.Constructor;
import java.util.Arrays;

/**
 * Internally stored as a Bundle for backwards compatibility<br>
 * but interface limited to PersistableBundle for forwards compatibility.<br>
 * Easily converted to Bundle<br>
 * Easily converted to PersistableBundle<br>
 */
public class JobParams {

    private enum ParamType {
        BOOLEAN( false ),
        BOOLEAN_ARRAY( true ),
        INTEGER( false ),
        INTEGER_ARRAY( true ),
        LONG( false ),
        LONG_ARRAY( true ),
        DOUBLE( false ),
        DOUBLE_ARRAY( true ),
        STRING( false ),
        STRING_ARRAY( true ),
        BUNDLE( false );

        private boolean array = false;

        ParamType( boolean isArray ) {
            array = isArray;
        }

        static ParamType fromId( int id ) {
            return ParamType.values()[id];
        }

        boolean isArray() {
            return array;
        }

        int toId() {
            return ordinal();
        }
    }

    private static final String KEY_BUNDLE_TYPES = "__JOBPARAM_COMPAT_TYPES__";
    private static String indentation = "  ";
    private Bundle values;
    private Bundle types;

    public JobParams() {
        values = new Bundle();
        types = new Bundle();
        values.putBundle( KEY_BUNDLE_TYPES, types );
    }

    private JobParams( Bundle bundle ) {
        values = bundle;
        types = values.getBundle( KEY_BUNDLE_TYPES );
    }

    public static void toIntent( JobParams in, String key, Intent out ) {
        out.putExtra( key, in.values );
    }

    public static JobParams fromIntent( String key, Intent in ) {
        Bundle b = in.getBundleExtra( key );
        if ( b != null ) {
            return new JobParams( b );
        }
        return null;
    }

    public static Bundle toBundle( JobParams params ) {
        return params.values;
    }

    /**
     * Only works for bundles that were previously JobParms.<br>
     * If any mutations to the bundle occurred outside of JobParams, an error will likely be thrown
     * <br>
     * This bundle should only be obtained via {@link JobParams#toBundle(JobParams)} as special meta
     * data is embeded and required by the system.
     */
    public static JobParams fromBundle( Bundle bundle ) {
        if ( !bundle.containsKey( KEY_BUNDLE_TYPES ) ) {
            throw new IllegalArgumentException( "This bundle is not a JobParams bundle" );
        }
        return new JobParams( bundle );
    }

    @TargetApi( Build.VERSION_CODES.LOLLIPOP_MR1 )
    @RequiresApi( api = Build.VERSION_CODES.LOLLIPOP )
    public static PersistableBundle toPersistableBundle( JobParams bundle ) {
        return toPersistableBundle( bundle.values );
    }

    @TargetApi( Build.VERSION_CODES.LOLLIPOP_MR1 )
    @RequiresApi( api = Build.VERSION_CODES.LOLLIPOP )
    private static PersistableBundle toPersistableBundle( Bundle bundle ) {
        if ( bundle == null ) {
            return null;
        }
        PersistableBundle pbundle = new PersistableBundle();
        PersistableBundle ptypes = new PersistableBundle();
        pbundle.putPersistableBundle( KEY_BUNDLE_TYPES, ptypes );

        Bundle types = bundle.getBundle( KEY_BUNDLE_TYPES );
        for ( String key : types.keySet() ) {
            ParamType type = ParamType.fromId( types.getInt( key ) );
            ptypes.putInt( key, type.toId() );

            switch ( type ) {
                case BOOLEAN:
                    pbundle.putBoolean( key, bundle.getBoolean( key ) );
                    break;
                case BOOLEAN_ARRAY:
                    pbundle.putBooleanArray( key, bundle.getBooleanArray( key ) );
                    break;
                case INTEGER:
                    pbundle.putInt( key, bundle.getInt( key ) );
                    break;
                case INTEGER_ARRAY:
                    pbundle.putIntArray( key, bundle.getIntArray( key ) );
                    break;
                case LONG:
                    pbundle.putLong( key, bundle.getLong( key ) );
                    break;
                case LONG_ARRAY:
                    pbundle.putLongArray( key, bundle.getLongArray( key ) );
                    break;
                case DOUBLE:
                    pbundle.putDouble( key, bundle.getDouble( key ) );
                    break;
                case DOUBLE_ARRAY:
                    pbundle.putDoubleArray( key, bundle.getDoubleArray( key ) );
                    break;
                case STRING:
                    pbundle.putString( key, bundle.getString( key ) );
                    break;
                case STRING_ARRAY:
                    pbundle.putStringArray( key, bundle.getStringArray( key ) );
                    break;
                case BUNDLE:
                    Bundle tmp = bundle.getBundle( key );
                    PersistableBundle p = toPersistableBundle( tmp );
                    pbundle.putPersistableBundle( key, p );
                    break;
            }
        }
        return pbundle;
    }

    /**
     * Only works for bundles that were previously JobParms.<br>
     * If any mutations to the bundle occurred outside of JobParams, an error will likely be thrown
     * <br>
     * This bundle should only be obtained via {@link JobParams#toBundle(JobParams)} as special meta
     * data is embeded and required by the system.
     */
    @TargetApi( Build.VERSION_CODES.LOLLIPOP_MR1 )
    @RequiresApi( api = Build.VERSION_CODES.LOLLIPOP )
    public static JobParams fromPersistableBundle( PersistableBundle bundle ) {
        if ( bundle == null ) {
            return null;
        }
        PersistableBundle types = bundle.getPersistableBundle( KEY_BUNDLE_TYPES );

        JobParams params = new JobParams();
        for ( String key : types.keySet() ) {
            ParamType type = ParamType.fromId( types.getInt( key ) );
            switch ( type ) {
                case BOOLEAN:
                    params.putBoolean( key, bundle.getBoolean( key ) );
                    break;
                case BOOLEAN_ARRAY:
                    params.putBooleanArray( key, bundle.getBooleanArray( key ) );
                    break;
                case INTEGER:
                    params.putInt( key, bundle.getInt( key ) );
                    break;
                case INTEGER_ARRAY:
                    params.putIntArray( key, bundle.getIntArray( key ) );
                    break;
                case LONG:
                    params.putLong( key, bundle.getLong( key ) );
                    break;
                case LONG_ARRAY:
                    params.putLongArray( key, bundle.getLongArray( key ) );
                    break;
                case DOUBLE:
                    params.putDouble( key, bundle.getDouble( key ) );
                    break;
                case DOUBLE_ARRAY:
                    params.putDoubleArray( key, bundle.getDoubleArray( key ) );
                    break;
                case STRING:
                    params.putString( key, bundle.getString( key ) );
                    break;
                case STRING_ARRAY:
                    params.putStringArray( key, bundle.getStringArray( key ) );
                    break;
                case BUNDLE:
                    PersistableBundle tmp = bundle.getPersistableBundle( key );
                    JobParams jp = fromPersistableBundle( tmp );
                    params.putBundle( key, jp );
                    break;
            }
        }
        return params;
    }

    @RequiresApi( api = Build.VERSION_CODES.LOLLIPOP )
    public static JobParams fromJobParameters( String key, JobParameters in ) {
        return fromPersistableBundle( in.getExtras().getPersistableBundle( key ) );
    }

    @RequiresApi( api = Build.VERSION_CODES.LOLLIPOP )
    public static void toJobInfo( JobParams in, String key, JobInfo out ) {
        out.getExtras().putPersistableBundle( key, toPersistableBundle( in ) );
    }

    public static JobParams deepCopy( JobParams in ) {
        if ( in == null ) {
            return null;
        }
        Bundle types = in.types;

        JobParams out = new JobParams();
        for ( String key : types.keySet() ) {
            ParamType type = ParamType.fromId( types.getInt( key ) );
            switch ( type ) {
                case BOOLEAN:
                    out.putBoolean( key, in.getBoolean( key ) );
                    break;
                case BOOLEAN_ARRAY:
                    out.putBooleanArray( key, in.getBooleanArray( key ) );
                    break;
                case INTEGER:
                    out.putInt( key, in.getInt( key ) );
                    break;
                case INTEGER_ARRAY:
                    out.putIntArray( key, in.getIntArray( key ) );
                    break;
                case LONG:
                    out.putLong( key, in.getLong( key ) );
                    break;
                case LONG_ARRAY:
                    out.putLongArray( key, in.getLongArray( key ) );
                    break;
                case DOUBLE:
                    out.putDouble( key, in.getDouble( key ) );
                    break;
                case DOUBLE_ARRAY:
                    out.putDoubleArray( key, in.getDoubleArray( key ) );
                    break;
                case STRING:
                    out.putString( key, in.getString( key ) );
                    break;
                case STRING_ARRAY:
                    out.putStringArray( key, in.getStringArray( key ) );
                    break;
                case BUNDLE:
                    JobParams jp = deepCopy( in.getBundle( key ) );
                    if ( jp != null ) {
                        out.putBundle( key, jp );
                    }
                    break;
            }
        }
        return out;
    }

    /**
     * Return the string value -- in the case of values that are JobParams, true is returned when the
     * key is present.
     *
     * @return defaultValue is returned when a key is not present
     */
    public static String getStringValue( JobParams in, String key, String defaultValue ) {
        if ( in == null ) {
            return null;
        }
        if ( !in.types.containsKey( key ) ) {
            return defaultValue;
        }

        Bundle types = in.types;
        ParamType type = ParamType.fromId( types.getInt( key ) );
        switch ( type ) {
            case BOOLEAN:
                return String.valueOf( in.getBoolean( key ) );
            case BOOLEAN_ARRAY:
                return Arrays.toString( in.getBooleanArray( key ) );
            case INTEGER:
                return String.valueOf( in.getInt( key ) );
            case INTEGER_ARRAY:
                return Arrays.toString( in.getIntArray( key ) );
            case LONG:
                return String.valueOf( in.getLong( key ) );
            case LONG_ARRAY:
                return Arrays.toString( in.getLongArray( key ) );
            case DOUBLE:
                return String.valueOf( in.getDouble( key ) );
            case DOUBLE_ARRAY:
                return Arrays.toString( in.getDoubleArray( key ) );
            case STRING:
                return String.valueOf( in.getString( key ) );
            case STRING_ARRAY:
                return Arrays.toString( in.getStringArray( key ) );
            case BUNDLE:
                return String.valueOf( true );
        }
        return defaultValue;
    }

    public static String describe( JobParams params ) {
        StringBuilder sb = new StringBuilder();
        describe( params, "", sb, "" );
        return sb.toString();
    }

    private static void describe( JobParams params, String rootKey, StringBuilder sb, String indent ) {
        String rootKeyColon = "";
        String rootKeyQuote = "";
        if ( rootKey != null && rootKey.length() > 0 ) {
            rootKeyColon = ":";
            rootKeyQuote = "'";
        }
        sb.append( indent );
        sb.append( rootKeyQuote + rootKey + rootKeyQuote + rootKeyColon + "{" );
        sb.append( "\n" );
        for ( String key : params.types.keySet() ) {
            ParamType type = ParamType.fromId( params.types.getInt( key ) );
            if ( ParamType.BUNDLE.equals( type ) ) {
                describe( params.getBundle( key ), key, sb, indentation + indent );
            } else {
                sb.append( indentation ).append( indent ).append( "'" + key + "':" );
                sb.append( JobParams.getStringValue( params, key, "" ) );
                sb.append( ",\n" );
            }
        }
        sb.append( indent ).append( "}" ).append( "\n" );
    }

    /**
     * Beware the key used in the internal bundle is based on the JobParamsVO type's simpleName<br>
     * The expectation is that there should only be one parameters value and there should be no<br>
     * collisions
     */
    public static <T extends JobParamsVO> void toIntent( T in, Intent out ) {
        JobParams.toIntent( in.params(), in.getClass().getSimpleName(), out );
    }

    /**
     * Beware the key used in the internal bundle is based on the JobParamsVO type's simpleName<br>
     * The expectation is that there should only be one parameters value and there should be no<br>
     * collisions
     */
    public static <T extends JobParamsVO> T fromIntent( Intent in, Class<T> type ) {
        try {
            JobParams params = JobParams.fromIntent( type.getSimpleName(), in );
            Constructor<T> constructor = type.getDeclaredConstructor( JobParams.class );
            constructor.setAccessible( true );
            T out = constructor.newInstance( params );
            return out;
        } catch ( Exception e ) {
            throw new IllegalStateException( "", e ); // epic usage fail just crash
        }
    }

    /**
     * Beware the key used in the internal bundle is based on the JobParamsVO type's simpleName<br>
     * The expectation is that there should only be one parameters value and there should be no<br>
     * collisions
     */
    @RequiresApi( api = Build.VERSION_CODES.LOLLIPOP )
    public static <T extends JobParamsVO> void toJobInfo( T in, JobInfo out ) {
        JobParams.toJobInfo( in.params(), in.getClass().getSimpleName(), out );
    }

    /**
     * Beware the key used in the internal bundle is based on the JobParamsVO type's simpleName<br>
     * The expectation is that there should only be one parameters value and there should be no<br>
     * collisions
     */
    @RequiresApi( api = Build.VERSION_CODES.LOLLIPOP )
    public static <T extends JobParamsVO> T fromJobParameters( JobParameters in, Class<T> type ) {
        try {
            JobParams params = JobParams.fromJobParameters( type.getSimpleName(), in );
            Constructor<T> constructor = type.getDeclaredConstructor( JobParams.class );
            constructor.setAccessible( true );
            T out = constructor.newInstance( params );
            return out;
        } catch ( Exception e ) {
            throw new IllegalStateException( "", e ); // epic usage fail just crash
        }
    }

    public void putBoolean( String key, boolean value ) {
        values.putBoolean( key, value );
        types.putInt( key, ParamType.BOOLEAN.toId() );
    }

    public boolean getBoolean( String key ) {
        return values.getBoolean( key );
    }

    public boolean getBoolean( String key, boolean defaultValue ) {
        return values.getBoolean( key, defaultValue );
    }

    public void putBooleanArray( String key, boolean[] value ) {
        values.putBooleanArray( key, value );
        types.putInt( key, ParamType.BOOLEAN_ARRAY.toId() );
    }

    public boolean[] getBooleanArray( String key ) {
        return values.getBooleanArray( key );
    }

    public void putInt( String key, int value ) {
        values.putInt( key, value );
        types.putInt( key, ParamType.INTEGER.toId() );
    }

    public int getInt( String key ) {
        return values.getInt( key );
    }

    public int getInt( String key, int defaultValue ) {
        return values.getInt( key, defaultValue );
    }

    public void putIntArray( String key, int[] value ) {
        values.putIntArray( key, value );
        types.putInt( key, ParamType.INTEGER_ARRAY.toId() );
    }

    public int[] getIntArray( String key ) {
        return values.getIntArray( key );
    }

    public void putLong( String key, long value ) {
        values.putLong( key, value );
        types.putInt( key, ParamType.LONG.toId() );
    }

    public long getLong( String key ) {
        return values.getLong( key );
    }

    public long getLong( String key, long defaultValue ) {
        return values.getLong( key, defaultValue );
    }

    public void putLongArray( String key, long[] value ) {
        values.putLongArray( key, value );
        types.putInt( key, ParamType.LONG_ARRAY.toId() );
    }

    public long[] getLongArray( String key ) {
        return values.getLongArray( key );
    }

    public void putDouble( String key, double value ) {
        values.putDouble( key, value );
        types.putInt( key, ParamType.DOUBLE.toId() );
    }

    public double getDouble( String key ) {
        return values.getDouble( key );
    }

    public double getDouble( String key, double defaultValue ) {
        return values.getDouble( key, defaultValue );
    }

    public void putDoubleArray( String key, double[] value ) {
        values.putDoubleArray( key, value );
        types.putInt( key, ParamType.DOUBLE_ARRAY.toId() );
    }

    public double[] getDoubleArray( String key ) {
        return values.getDoubleArray( key );
    }

    public void putString( String key, String value ) {
        values.putString( key, value );
        types.putInt( key, ParamType.STRING.toId() );
    }

    public String getString( String key ) {
        return values.getString( key );
    }

    public String getString( String key, String defaultValue ) {
        return values.getString( key, defaultValue );
    }

    public void putStringArray( String key, String[] value ) {
        values.putStringArray( key, value );
        types.putInt( key, ParamType.STRING_ARRAY.toId() );
    }

    public String[] getStringArray( String key ) {
        return values.getStringArray( key );
    }

    public void putBundle( String key, JobParams bundle ) {
        values.putBundle( key, bundle.values );
        types.putInt( key, ParamType.BUNDLE.toId() );
    }

    public JobParams getBundle( String key ) {
        Bundle b = values.getBundle( key );
        if ( b != null ) {
            return new JobParams( b );
        }
        return null;
    }

    static void test() {
        JobParams p = new JobParams();
        p.putBoolean( "boolean", true );
        p.putBooleanArray( "boolean_array", new boolean[]{ true, false, true } );
        p.putInt( "int", 1 );
        p.putIntArray( "int_array", new int[]{ 1, 0, 1 } );
        p.putLong( "long", 1L );
        p.putLongArray( "long_array", new long[]{ 1L, 0L, 1L } );
        p.putDouble( "double", 1.0 );
        p.putDoubleArray( "double_array", new double[]{ 1.0, 0.0, 1.0 } );
        p.putString( "string", "hello" );
        p.putStringArray( "string_array", new String[]{ "HELLO", "hello", "HELLO" } );
        p.putBundle( "bundle", deepCopy( p ) );

        JobParams copy = deepCopy( p ); // now with the nested bundle
        Bundle b = JobParams.toBundle( copy );
        copy = JobParams.fromBundle( b );

        if ( android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP ) {
            PersistableBundle pb = null;
            pb = JobParams.toPersistableBundle( copy );
            copy = JobParams.fromPersistableBundle( pb );
        }

        Intent intent = new Intent();
        JobParams.toIntent( copy, "thekey", intent );
        copy = JobParams.fromIntent( "thekey", intent );

        Log.d( JobParams.class.getSimpleName(), JobParams.describe( copy ) );
    }
}
