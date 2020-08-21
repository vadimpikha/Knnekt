package knnekt.shared.data.util

import android.content.SharedPreferences
import android.os.Parcelable
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

@Suppress("UNCHECKED_CAST")
class PreferencesDelegate<TValue>(
    private val preferences: SharedPreferences,
    private val name: String,
    private val defValue: TValue,
    private val serializer: StringSerializer<TValue>?
) : ReadWriteProperty<Any?, TValue> {

    override fun getValue(thisRef: Any?, property: KProperty<*>): TValue {
        with(preferences) {

            if (serializer != null) {
                val string = getString(name, null) ?: return defValue
                return serializer.fromString(string)
            }

            return when (defValue) {
                is Boolean -> (getBoolean(name, defValue) as? TValue) ?: defValue
                is Int -> (getInt(name, defValue) as TValue) ?: defValue
                is Float -> (getFloat(name, defValue) as TValue) ?: defValue
                is Long -> (getLong(name, defValue) as TValue) ?: defValue
                is String -> (getString(name, defValue) as TValue) ?: defValue
                else -> throw NotFoundRealizationException(defValue)
            }
        }
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: TValue) {
        with(preferences.edit()) {

            if (serializer != null) {
                putString(name, serializer.toString(value))
            } else {
                when (value) {
                    is Boolean -> putBoolean(name, value)
                    is Int -> putInt(name, value)
                    is Float -> putFloat(name, value)
                    is Long -> putLong(name, value)
                    is String -> putString(name, value)
                    else -> throw NotFoundRealizationException(value)
                }
            }

            apply()
        }
    }

    class NotFoundRealizationException(defValue: Any?) :
        Exception("not found realization for $defValue")
}

interface StringSerializer<T> {
    fun toString(value: T): String
    fun fromString(string: String): T
}

fun <T> SharedPreferences.delegate(key: String, def: T, serializer: StringSerializer<T>? = null) =
    PreferencesDelegate(this, key, def, serializer)