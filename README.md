# DemoAndroidIntentRCE

This project demonstrates that calls to getExtra and related methods do not increase the risk of RCE via deserialization in Android.

Some security analysis software will report that any call to get an extra from a Bundle in an Android app could result in a remote code execution vulnerability, but that is false.

## Background

In Android 13 the following change was made to the OS:

Lazy bundle: https://cs.android.com/android/_/android/platform/frameworks/base/+/9ca6a5e21a1987fd3800a899c1384b22d23b6dee

Calling Intent.getExtra* of any kind does not possibly trigger any RCE risk in Android 13 and later.

In Android 12 and prior calling finish() will trigger a call to unparcel automatically, the author of the program cannot prevent this since the unparcel call happens inside the implementation of the Android OS.

## Demonstration

Build and install the example application in this repository.

This project contains two activities:
 - TargetActivity: It is through this activity that an attacker will attempt to trigger a remote code execution vulnerability
 - AttackerActivity: Although this activity is in the same app as the target activity that is only to keep this demo simple, this activity could easily be located in a different app

To demonstrate the vulnerability run this app on an Android device, notice that the behavior in Android 13 is different from prior Android versions.

Tap the mail button in the first activity to begin the attack which will start the target activity with a parcelable that takes advantage of an RCE vulnerability via parcelables. In the target activity tap the Done button to conclude the demo and observe the logs to see if the attack succeeded.

Example from Android 13, notice the CustomParcelable is not deserialized, even when calling Intent.getStringExtra(), the possibility of RCE attack is therefore not related to the call to Intent.getStringExtra and similar methods.

```
2024-05-13 13:39:11.037  9816  9816 I JWA/TargetActivity: Incoming intent has string extra: Hello
2024-05-13 13:39:14.103  9816  9816 I JWA/TargetActivity: Calling finish
```

Example from Android 10, notice that CustomParcelable is always deserialized when calling Activity.finish, the possibility of RCE attack is therefore not related to the call to Intent.getStringExtra and similar methods.

```
2024-05-13 13:37:26.576 11181 11181 I JWA/TargetActivity: Calling finish
2024-05-13 13:37:26.595 11181 11181 W JWA/VulnerableParcelable: Executing command log attacker RCE success
2024-05-13 13:37:26.642 11639 11639 I log     : attacker RCE success
2024-05-13 13:37:26.650 11181 11181 W JWA/VulnerableParcelable: Attack succeeded, command was executed
2024-05-13 13:37:26.650 11181 11181 W JWA/VulnerableParcelable: java.lang.Exception: trace
2024-05-13 13:37:26.650 11181 11181 W JWA/VulnerableParcelable: 	at org.jwa.demoandroidintentrce.VulnerableParcelable.<init>(VulnerableParcelable.java:33)
2024-05-13 13:37:26.650 11181 11181 W JWA/VulnerableParcelable: 	at org.jwa.demoandroidintentrce.VulnerableParcelable.<init>(VulnerableParcelable.java:14)
2024-05-13 13:37:26.650 11181 11181 W JWA/VulnerableParcelable: 	at org.jwa.demoandroidintentrce.VulnerableParcelable$1.createFromParcel(VulnerableParcelable.java:57)
2024-05-13 13:37:26.650 11181 11181 W JWA/VulnerableParcelable: 	at org.jwa.demoandroidintentrce.VulnerableParcelable$1.createFromParcel(VulnerableParcelable.java:55)
2024-05-13 13:37:26.650 11181 11181 W JWA/VulnerableParcelable: 	at android.os.Parcel.readParcelable(Parcel.java:2974)
2024-05-13 13:37:26.650 11181 11181 W JWA/VulnerableParcelable: 	at android.os.Parcel.readValue(Parcel.java:2867)
2024-05-13 13:37:26.650 11181 11181 W JWA/VulnerableParcelable: 	at android.os.Parcel.readArrayMapInternal(Parcel.java:3245)
2024-05-13 13:37:26.650 11181 11181 W JWA/VulnerableParcelable: 	at android.os.BaseBundle.initializeFromParcelLocked(BaseBundle.java:292)
2024-05-13 13:37:26.650 11181 11181 W JWA/VulnerableParcelable: 	at android.os.BaseBundle.unparcel(BaseBundle.java:236)
2024-05-13 13:37:26.650 11181 11181 W JWA/VulnerableParcelable: 	at android.os.BaseBundle.containsKey(BaseBundle.java:509)
2024-05-13 13:37:26.650 11181 11181 W JWA/VulnerableParcelable: 	at android.content.Intent.hasExtra(Intent.java:7746)
2024-05-13 13:37:26.650 11181 11181 W JWA/VulnerableParcelable: 	at android.app.Activity.finish(Activity.java:6268)
2024-05-13 13:37:26.650 11181 11181 W JWA/VulnerableParcelable: 	at android.app.Activity.finish(Activity.java:6280)
2024-05-13 13:37:26.650 11181 11181 W JWA/VulnerableParcelable: 	at org.jwa.demoandroidintentrce.TargetActivity.lambda$onCreate$0$org-jwa-demoandroidintentrce-TargetActivity(TargetActivity.java:41)
2024-05-13 13:37:26.650 11181 11181 W JWA/VulnerableParcelable: 	at org.jwa.demoandroidintentrce.TargetActivity$$ExternalSyntheticLambda0.onClick(D8$$SyntheticClass:0)
```

## Conclusion

The conclusion calling Intent.getStringExtra and similar methods does not affect the risk of an RCE in any circumstance!

Recommendations to remove/alter calls to Intent.getExtra methods to reduce the risk of RCE are therefore unfounded and are definitively false positives.
