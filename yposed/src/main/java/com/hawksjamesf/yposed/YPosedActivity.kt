package com.hawksjamesf.yposed

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.annotation.Keep
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_yposed.*
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*

class YPosedActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_yposed)
        bt_hook_frida.setOnClickListener {
            bt_hook_frida.text = stringFromJNI()
        }
        val info = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
        PackageManager.GET_META_DATA
        info.signatures[0].toByteArray()
        info.signatures[0].toCharsString()
        Log.d("cjf", "YPosedActivity firstInstallTime:${info.firstInstallTime} lastUpdateTime:${info.lastUpdateTime}")
        val cert = info.signatures[0].toByteArray()
        Log.d("cjf", "YPosedActivity sha1:" + sha1ToHexString(cert))
        Log.d("cjf", "jni check sign:v1:${checkSign(this)}")
        Log.d("cjf", "jni check sign:v1:${checkSign(this)}  v2 ${checkSignv2(this)}")
//        getAllCalssz(classLoader)

    }

    fun getAllCalssz(cl: ClassLoader) {
        try {
            val f = cl::class.java.getDeclaredField("classes")
            f.isAccessible = true
//            val classLoader = Thread.currentThread().contextClassLoader
//            val allCls: List<Class<*>>? = f.get(classLoader) as? List<Class<*>> ?: return
//            for (cls in allCls!!) {
//                val location = cls.getResource('/'.toString() + cls.name.replace('.', '/') + ".class")
//                Log.d("cjf", "<p>$location<p/>")
//            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun stringFromJava() = "string  from java"

    @Keep
    external fun stringFromJNI(): String

    @Keep
    external fun checkSign(ctx: Context): Boolean
    @Keep
    external fun checkSignv2(ctx: Context): Boolean

    companion object {

        init {
            System.loadLibrary("hawks")

        }

    }


}
