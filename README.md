# JobServiceAndJobIntentService
Example of how to simplify the difference between Service and JobService, IntentService and JobIntentService

Check out these points of interest:

# [LaunchActivity.java](https://github.com/aarontharris/JobServiceAndJobIntentService/blob/master/app/src/main/java/itch/ath/com/itchyscratchy/LaunchActivity.java)

Main entry point to the example app

# [MyService.java](https://github.com/aarontharris/JobServiceAndJobIntentService/blob/master/app/src/main/java/itch/ath/com/itchyscratchy/serviceexample/MyService.java)

My Service Delegate -- Service and JobService delegate to this central point.

# [ApiHelper.java](https://github.com/aarontharris/JobServiceAndJobIntentService/blob/master/app/src/main/java/itch/ath/com/itchyscratchy/commonstuff/ApiHelper.java)

A utility to simplify starting a service by API version and also obtain a delegate by API version.

# [MyJobIntentService.java](https://github.com/aarontharris/JobServiceAndJobIntentService/blob/master/app/src/main/java/itch/ath/com/itchyscratchy/intentserviceexample/MyJobIntentService.java)

My JobIntentService -- no delegate needed since JobIntentService is backwards compatible

# [JobParams.Java](https://github.com/aarontharris/JobServiceAndJobIntentService/blob/master/app/src/main/java/itch/ath/com/itchyscratchy/commonstuff/JobParams.java) and [JobParamsVO.java](https://github.com/aarontharris/JobServiceAndJobIntentService/blob/master/app/src/main/java/itch/ath/com/itchyscratchy/commonstuff/JobParamsVO.java)

An API agnostic Bundle that can convert to and from the old Bundle and new PersistableBundle.
