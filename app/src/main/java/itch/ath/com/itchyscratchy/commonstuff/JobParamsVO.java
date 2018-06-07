package itch.ath.com.itchyscratchy.commonstuff;

public class JobParamsVO {
    private JobParams params;

    protected JobParamsVO( JobParams params ) {
        this.params = params;
    }

    protected JobParamsVO() {
        params = new JobParams();
    }

    protected JobParams params() {
        return params;
    }
}
