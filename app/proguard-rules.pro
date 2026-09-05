# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# =====================================================================
# 1. Attributes & Generics (ضروري جداً لـ Gson, Retrofit & Coroutines)
# =====================================================================
-keepattributes Signature, InnerClasses, EnclosingMethod, *Annotation*
-dontwarn org.slf4j.impl.StaticLoggerBinder

# =====================================================================
# 2. Gson & TypeToken (حل كراش TypeToken الشامل)
# =====================================================================
-keep class com.google.gson.** { *; }
-keep class com.google.gson.reflect.TypeToken
-keep class * extends com.google.gson.reflect.TypeToken
-keepclassmembers class * extends com.google.gson.reflect.TypeToken { *; }
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}
-dontwarn com.google.gson.**

# =====================================================================
# 3. Models & Entities (حماية طبقة البيانات بالكامل)
# =====================================================================
-keep class com.example.applicationhome.data.** { *; }

# =====================================================================
# 4. Retrofit & Firebase Database
# =====================================================================
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepclassmembers class * {
    @retrofit2.http.* <methods>;
}

-keep class com.google.firebase.database.** { *; }
-dontwarn com.google.firebase.database.**

# =====================================================================
# 5. Android Architecture & Parcelable (لـ Jetpack Compose & Navigation)
# =====================================================================
-keep class * extends androidx.work.ListenableWorker { *; }

-keepclassmembers class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}