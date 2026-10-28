# --- General Android & Kotlin ---
-keepattributes *Annotation*,Signature,InnerClasses,EnclosingMethod
-keepclassmembers class * {
    @androidx.annotation.Keep *;
}
-dontwarn java.lang.invoke.**
-dontwarn javax.annotation.**

# --- Kotlinx Serialization ---
-keepattributes *Annotation*,Signature
-keepclassmembers class * {
    *** Companion;
}
-keepclasseswithmembers class * {
    kotlinx.serialization.KSerializer serializer(...);
}
-keep,allowobfuscation,allowshrinking class * {
    <init>(...);
}
-keepclassmembers class * {
    @kotlinx.serialization.Serializable <fields>;
}
-keepclassmembers class com.skylink.sksweetsandbakes.domain.model.** { *; }
-keepclassmembers class com.skylink.sksweetsandbakes.data.orders.requestmodel.** { *; }
-keepclassmembers class com.skylink.sksweetsandbakes.data.orders.responsemodel.** { *; }
-keepclassmembers class com.skylink.sksweetsandbakes.data.auth.requestmodel.** { *; }
-keepclassmembers class com.skylink.sksweetsandbakes.data.auth.responsemodel.** { *; }

# --- Razorpay SDK ---
-keep class com.razorpay.** {*;}
-dontwarn com.razorpay.**
-keepattributes JavascriptInterface
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

# --- Cashfree SDK ---
-keep class com.cashfree.pg.** { *; }
-dontwarn com.cashfree.pg.**

# --- Coil Image Loading ---
-dontwarn coil.**
-keep class coil.** { *; }

# --- Firebase & Google Play Services ---
-keep class com.google.firebase.** { *; }
-dontwarn com.google.firebase.**
-keep class com.google.android.gms.** { *; }
-dontwarn com.google.android.gms.**

# --- Facebook SDK ---
-keep class com.facebook.** { *; }
-dontwarn com.facebook.**

# --- Jetpack Compose & Material Icons ---
-keep class androidx.compose.material.icons.** { *; }

-classobfuscationdictionary package-dictionary.txt
-obfuscationdictionary obfuscation_dictionary.txt
