package com.ezlife.lib.sharepref

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import org.json.JSONArray

/**
 * Author datnv
 */
class Pref {
    private var prefs: SharedPreferences

    private constructor(context: Context) {
        prefs = context.applicationContext.getSharedPreferences(
            context.getString(R.string.app_name),
            Context.MODE_PRIVATE
        )
    }

    private constructor(context: Context, sharePreferencesName: String) {
        prefs = context.applicationContext.getSharedPreferences(
            sharePreferencesName,
            Context.MODE_PRIVATE
        )
    }

    companion object {
        private var instance: Pref? = null
        private fun getInstance(): Pref? {
            requireNotNull(instance) { "Pref must be call initHelper on Application before using." }
            return instance
        }

        fun initHelper(context: Context): Pref? {
            if (instance == null) instance = Pref(context)
            return instance
        }

        fun initHelper(context: Context, sharePreferencesName: String): Pref? {
            if (instance == null) instance = Pref(context, sharePreferencesName)
            return instance
        }

        fun setVal(key: String?, value: Boolean) {
            getInstance()!!.prefs.edit().putBoolean(key, value).apply()
        }

        fun setVal(key: String?, value: String?) {
            getInstance()!!.prefs.edit().putString(key, value).apply()
        }

        fun setVal(key: String?, value: Any?) {
            getInstance()!!.prefs.edit().putString(key, com.google.gson.Gson().toJson(value))
                .apply()
        }

        fun setVal(key: String?, value: Int) {
            getInstance()!!.prefs.edit().putInt(key, value).apply()
        }

        fun setVal(key: String?, value: Long) {
            getInstance()!!.prefs.edit().putLong(key, value).apply()
        }

        fun setVal(key: String?, value: Float) {
            getInstance()!!.prefs.edit().putFloat(key, value).apply()
        }

        fun setVal(key: String?, value: Double) {
            setVal(key, value.toString())
        }

        fun <T> setVal(key: String?, list: List<T>?) {
            setVal(key, com.google.gson.Gson().toJson(list))
        }

        fun <K, V> setVal(key: String?, map: Map<K, V>?) {
            setVal(key, com.google.gson.Gson().toJson(map))
        }

        fun <T> setVal(key: String?, array: Array<T>) {
            val jArray = JSONArray()
            for (t in array) {
                jArray.put(t)
            }
            getInstance()!!.prefs.edit().putString(key, com.google.gson.Gson().toJson(jArray))
                .apply()
        }

        fun <T> getArrayVal(key: String?): Array<T>? {
            val results: Array<T>? = null
            try {
                val jArray = JSONArray(getInstance()!!.prefs.getString(key, ""))
                for (i in 0 until jArray.length()) {
                    results!![i] = jArray[i] as T
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
            return results
        }

        fun <T> getListVal(key: String?): List<T>? {
            var objects: List<T>? = null
            try {
                val obj = getInstance()!!.prefs.getString(key, "")
                objects = com.google.gson.Gson().fromJson<List<T>>(
                    obj,
                    object : com.google.gson.reflect.TypeToken<List<T>?>() {}.getType()
                )
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
            return objects
        }

        fun <K, V> getMapVal(key: String?): Map<K, V>? {
            var objects: Map<K, V>? = null
            try {
                val obj = getInstance()!!.prefs.getString(key, "")
                objects = com.google.gson.Gson().fromJson<Map<K, V>>(
                    obj,
                    object : com.google.gson.reflect.TypeToken<Map<K, V>?>() {}.getType()
                )
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
            return objects
        }

        fun getBooleanVal(key: String?, defvalue: Boolean): Boolean {
            return getInstance()!!.prefs.getBoolean(key, defvalue)
        }

        fun getBooleanVal(key: String?): Boolean {
            return getInstance()!!.prefs.getBoolean(key, false)
        }

        fun getStringVal(key: String?, defvalue: String?): String? {
            return getInstance()!!.prefs.getString(key, defvalue)
        }

        fun getStringVal(key: String?): String? {
            return getInstance()!!.prefs.getString(key, null)
        }

        fun <T> getObjectVal(key: String?, mModelClass: Class<T>?): T {
            var `object`: Any? = null
            try {
                `object` = com.google.gson.Gson()
                    .fromJson<T>(getInstance()!!.prefs.getString(key, ""), mModelClass)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
            return com.google.gson.internal.Primitives.wrap<T>(mModelClass).cast(`object`)
        }

        fun getIntVal(key: String?, defVal: Int): Int {
            return getInstance()!!.prefs.getInt(key, defVal)
        }

        fun getIntVal(key: String?): Int {
            return getInstance()!!.prefs.getInt(key, 0)
        }

        fun getLongVal(key: String?, defVal: Long): Long {
            return getInstance()!!.prefs.getLong(key, defVal)
        }

        fun getLongVal(key: String?): Long {
            return getInstance()!!.prefs.getLong(key, 0)
        }

        fun getFloatVal(key: String?, defVal: Float): Float {
            return getInstance()!!.prefs.getFloat(key, defVal)
        }

        fun getFloatVal(key: String?): Float {
            return getInstance()!!.prefs.getFloat(key, 0f)
        }

        fun getDoubleVal(key: String?, defVal: Double): Double {
            return getStringVal(key, defVal.toString())!!.toDouble()
        }

        fun getDoubleVal(key: String?): Double {
            return getStringVal(key, 0.toString())!!.toDouble()
        }

        val all: Map<String, *>
            get() = getInstance()!!.prefs.all

        fun removeKey(key: String?) {
            getInstance()!!.prefs.edit().remove(key).apply()
        }

        fun removeAllKeys() {
            getInstance()!!.prefs.edit().clear().apply()
        }

        fun contain(key: String?): Boolean {
            return getInstance()!!.prefs.contains(key)
        }

        fun registerChangeListener(listener: OnSharedPreferenceChangeListener?) {
            getInstance()!!.prefs.registerOnSharedPreferenceChangeListener(listener)
        }

        fun unregisterChangeListener(listener: OnSharedPreferenceChangeListener?) {
            getInstance()!!.prefs.unregisterOnSharedPreferenceChangeListener(listener)
        }
    }
}