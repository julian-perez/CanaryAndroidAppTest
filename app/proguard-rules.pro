####################################################################################################
####################################################################################################
####################################################################################################
######################################### PROGUARD #################################################
####################################################################################################
####################################################################################################
####################################################################################################
 
# This is a configuration file for ProGuard.
# http://proguard.sourceforge.net/index.html#manual/usage.html
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose
-ignorewarnings
 
# Optimization is turned off by default. Dex does not like code run
# through the ProGuard optimize and preverify steps (and performs some
# of these optimizations on its own).
-dontoptimize
#-dontpreverify
 
# If you want to enable optimization, you should include the
# following:
-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*
-optimizationpasses 5
-allowaccessmodification
#
# Note that you cannot just include these flags in your own
# configuration file; if you are including this file, optimization
# will be turned off. You'll need to either edit this file, or
# duplicate the contents of this file and remove the include of this
# file from your project's proguard.config path property.
 
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgent
-keep public class * extends android.preference.Preference
-keep public class * extends android.support.v4.app.Fragment
-keep public class * extends android.support.v4.app.DialogFragment
-keep public class * extends com.actionbarsherlock.app.SherlockListFragment
-keep public class * extends com.actionbarsherlock.app.SherlockFragment
-keep public class * extends com.actionbarsherlock.app.SherlockFragmentActivity
-keep public class * extends android.app.Fragment
-keep public class com.android.vending.licensing.ILicensingServicegoogl
-keep class com.google.android.gms.internal.** { *; }
-keep public class is.yranac.canary.nativelibs.ChirpManager{
   public <methods>;
}

-keep public class is.yranac.canary.services.watchlive.GstreamerPlayer{
   <methods>;
   <fields>;

}

-keep public class org.freedesktop.gstreamer.androidmedia.*{
   <methods>;
   <fields>;

}

# For native methods, see http://proguard.sourceforge.net/manual/examples.html#native
-keepclasseswithmembernames class * {
 native <methods>;
}
 
-keep public class * extends android.view.View {
 public <init>(android.content.Context);
 public <init>(android.content.Context, android.util.AttributeSet);
 public <init>(android.content.Context, android.util.AttributeSet, int);
 public void set*(...);
}
 
-keepclasseswithmembers class * {
 public <init>(android.content.Context, android.util.AttributeSet);
}
 
-keepclasseswithmembers class * {
 public <init>(android.content.Context, android.util.AttributeSet, int);
}
 
-keepclassmembers class * extends android.app.Activity {
 public void *(android.view.View);
}
 
# For enumeration classes, see http://proguard.sourceforge.net/manual/examples.html#enumerations
-keepclassmembers enum is.yranac.canary.** { *; }

 -keepclassmembers enum * {
     public static **[] values();
     public static ** valueOf(java.lang.String);
 }

-keep class * implements android.os.Parcelable {
 public static final android.os.Parcelable$Creator *;
}
 
-keepclassmembers class **.R$* {
 public static <fields>;
}


-keepclassmembers,allowobfuscation class is.yranac.canary.** {
    <methods>;
}

-keep class android.support.v4.app.** { *; }
-keep interface android.support.v4.app.** { *; }
-keep class com.actionbarsherlock.** { *; }
-keep interface com.actionbarsherlock.** { *; }
-keep class com.urbanairship.** { *; }
-keep interface com.urbanairship.** { *; }
-keep class org.apache.http.**
-keep class retrofit.** { *; }
-keep interface retrofit.** { *; }
-keep public interface com.zendesk.** { *; }
-keep public class com.zendesk.** { *; }
-keep interface com.zendesk.sdk.** { *; }
-keep class org.slf4j.** { *; }
-keepattributes EnclosingMethod

# We only want obfuscation
-keepattributes InnerClasses
-keepattributes Signature
-keepattributes Exceptions
-keepattributes *Annotation*
-keepattributes EnclosingMethod

# Sdk
-keep public interface com.zendesk.** { *; }
-keep public class com.zendesk.** { *; }
-dontwarn java.awt.**

# Appcompat and support
-keep interface android.support.v7.** { *; }
-keep class android.support.v7.** { *; }
-keep interface android.support.v4.** { *; }
-keep class android.support.v4.** { *; }
-dontwarn android.app.Notification
-dontwarn android.support.v4.**


# Gson
-keep interface com.google.gson.** { *; }
-keep class com.google.gson.** { *; }

# Retrofit
-keep class com.google.inject.** { *; }
-keep class org.apache.http.** { *; }
-keep class org.apache.james.mime4j.** { *; }
-keep class javax.inject.** { *; }
-keep class retrofit.** { *; }
-keep interface retrofit.** { *; }
-dontwarn rx.**
-dontwarn com.google.appengine.api.urlfetch.**
-dontwarn okio.**

-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**

#Picasso
-dontwarn com.squareup.okhttp.**

-keep interface org.slf4j.** { *; }
-keep class org.apache.** { *; }
-keep interface org.apache.** { *; }
-keep class com.google.gson.** { *; }
-keep class com.google.inject.* { *; }
-keep class org.apache.http.* { *; }
-keep class org.apache.james.mime4j.* { *; }
-keep class javax.inject.* { *; }
-keep class com.google.gson.** { *; }
-keep class com.google.inject.* { *; }
-keep class org.apache.http.* { *; }
-keep class org.apache.james.mime4j.* { *; }
-keep class javax.inject.* { *; }
-keep class com.newrelic.** { *; }
-keep class com.snowplowanalytics.* { *; }
-keep interface com.snowplowanalytics.* { *; }
-dontwarn com.snowplowanalytics.**
-keep class com.snowplowanalytics.**{
   *;
}

-keepattributes Exceptions, Signature, InnerClasses
-dontwarn rx.*
-keep class com.example.testobfuscation.** { *; }
-keepattributes Signature
-keep class sun.misc.Unsafe { *; }
-keepattributes *Annotation*
-dontwarn android.support.v7.**
-keep class android.support.v7.** { *; }
-keep interface android.support.v7.** { *; }

-keep class org.spongycastle.**

# The support library contains references to newer platform versions.
# Don't warn about those in case this app is linking against an older
# platform version. We know about them, and they are safe.
-dontwarn android.support.**
-dontwarn com.google.**
-dontwarn android.**
-dontwarn org.apache.**
-dontwarn javax.**
-dontwarn rx.**
-dontwarn java.**
-dontwarn com.squareup.**
-dontwarn org.codehaus.**
-keepattributes SourceFile,LineNumberTable
-dontwarn org.apache.http.**
-dontwarn java.lang.invoke**

-flattenpackagehierarchy
