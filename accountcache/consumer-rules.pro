-keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
public static java.lang.String TABLENAME;
}
-keep class **$Properties { *; }

# If you DO use SQLCipher:
-keep class org.greenrobot.greendao.database.SqlCipherEncryptedHelper { *; }

# If you do NOT use SQLCipher:
-dontwarn net.sqlcipher.database.**
# If you do NOT use RxJava:
-dontwarn rx.**
-keep class net.sqlcipher.** { *; }
-keep class rx.** { *; }
-keep class com.facebook.flipper.** { *; }
-keep class com.facebook.jni.** { *; }
