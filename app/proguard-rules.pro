# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/iwo/android-sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Keep native methods
-keepclassmembers class * {
    native <methods>;
}

-keep class android.support.v7.widget.** { *; }
-keep class android.widget.** { *; }


##---------------Begin: proguard configuration for Gson ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-dontwarn sun.misc.**
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { <fields>; }


##---------------Begin: proguard configuration for Retrofit ----------
# Retrofit does reflection on generic parameters. InnerClasses is required to use Signature and
# EnclosingMethod is required to use InnerClasses.
-keepattributes Signature, InnerClasses, EnclosingMethod

# Retrofit does reflection on method and parameter annotations.
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations

# Retain service method parameters when optimizing.
-keepclassmembers,allowshrinking,allowobfuscation interface * {
@retrofit2.http.* <methods>;
}

# Ignore JSR 305 annotations for embedding nullability information.
-dontwarn javax.annotation.**

# Guarded by a NoClassDefFoundError try/catch and only used when on the classpath.
-dontwarn kotlin.Unit

# Top-level functions that can only be used by Kotlin.
-dontwarn retrofit2.KotlinExtensions.*

# With R8 full mode, it sees no subtypes of Retrofit interfaces since they are created with a Proxy
# and replaces all potential values with null. Explicitly keeping the interfaces prevents this.
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>

-dontwarn kotlinx.**

# keep di class
-keep class app.keyboardly.addon.sample.di.** { *; }

-dontwarn com.google.errorprone.annotations.**

# keep the resource / raw file
-keep class *.R
-keep class dagger.* { *; }

-keepclasseswithmembers class **.R$* {
    public static <fields>;
}


#noinspection ShrinkerUnresolvedReference
-keep class app.keyboardly.addon.sample.DynamicFeatureImpl {
    #noinspection ShrinkerUnresolvedReference
    app.keyboardly.addon.sample.DynamicFeatureImpl$Provider Provider;
}
-keep class app.keyboardly.addon.sample.DynamicFeatureImpl$Provider {
    *;
}

#-------------------------------------------------
# JetPack Navigation
# This fixes: Caused by: androidx.fragment.app.Fragment$InstantiationException:
# Unable to instantiate fragment androidx.navigation.fragment.NavHostFragment: make sure class name exists
#-------------------------------------------------
-keepnames class androidx.navigation.fragment.NavHostFragment
-keepnames class androidx.navigation.dynamicfeatures.fragment.DynamicNavHostFragment
-keep class androidx.navigation.fragment.NavHostFragment
-keep class androidx.navigation.dynamicfeatures.fragment.DynamicNavHostFragment

-keep class * extends androidx.support.v4.app.Fragment{}
-keep class * extends androidx.navigation.dynamicfeatures.fragment.DynamicNavHostFragment{}