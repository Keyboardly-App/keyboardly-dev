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

# Ignore annotation used for build tooling.
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

# Ignore JSR 305 annotations for embedding nullability information.
-dontwarn javax.annotation.**

# Guarded by a NoClassDefFoundError try/catch and only used when on the classpath.
-dontwarn kotlin.Unit

# Top-level functions that can only be used by Kotlin.
-dontwarn retrofit2.KotlinExtensions
-dontwarn retrofit2.KotlinExtensions.*

# With R8 full mode, it sees no subtypes of Retrofit interfaces since they are created with a Proxy
# and replaces all potential values with null. Explicitly keeping the interfaces prevents this.
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>

-dontwarn kotlinx.**

# keep class data
-keep class app.keyboardly.addon.sample.** { *; }
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

# OkHttp platform used only on JVM and when Conscrypt and other security providers are available.
-dontwarn okhttp3.internal.platform.**
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**

-keep class app.keyboardly.addon.sample.data.** { *; }
-keep class app.keyboardly.addon.sample.data.model.** { *; }
-keep class app.keyboardly.addon.sample.data.model.Province
-keep class app.keyboardly.addon.sample.data.local.** { *; }
-keep class app.keyboardly.addon.sample.data.local.dao.**{ *; }
-keep class app.keyboardly.addon.sample.data.local.dao.ProvinceDao_Impl
-keep class app.keyboardly.addon.sample.data.remote.** { *; }
-keep class app.keyboardly.addon.sample.data.remote.ProvinceService

-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-dontwarn androidx.room.paging.**