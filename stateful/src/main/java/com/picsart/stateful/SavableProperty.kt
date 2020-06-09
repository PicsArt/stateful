/*
 * Copyright (C) 2019 PicsArt, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.picsart.stateful

import android.os.Bundle
import android.os.Parcelable
import com.google.gson.Gson
import java.io.Serializable
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class SavableProperty<T>(
        private val defaultValue: T,
        private val key: String
) : ReadWriteProperty<Any, T>, Savable {

    internal var value: T = defaultValue

    private val classKey = key + "_class"

    override fun saveState(bundle: Bundle) {
        val value: T? = value ?: return

        when (value) {
            is Boolean -> bundle.putBoolean(key, value as Boolean)
            is BooleanArray -> bundle.putBooleanArray(key, value as BooleanArray)
            is Bundle -> bundle.putBundle(key, value as Bundle)
            is Byte -> bundle.putByte(key, value as Byte)
            is ByteArray -> bundle.putByteArray(key, value as ByteArray)
            is Char -> bundle.putChar(key, value as Char)
            is CharArray -> bundle.putCharArray(key, value as CharArray)
            is Double -> bundle.putDouble(key, value as Double)
            is DoubleArray -> bundle.putDoubleArray(key, value as DoubleArray)
            is Float -> bundle.putFloat(key, value as Float)
            is FloatArray -> bundle.putFloatArray(key, value as FloatArray)
            is Int -> bundle.putInt(key, value as Int)
            is IntArray -> bundle.putIntArray(key, value as IntArray)
            is Long -> bundle.putLong(key, value as Long)
            is LongArray -> bundle.putLongArray(key, value as LongArray)
            is Parcelable -> bundle.putParcelable(key, value as Parcelable)
            is Short -> bundle.putShort(key, value as Short)
            is ShortArray -> bundle.putShortArray(key, value as ShortArray)
            is String -> bundle.putString(key, value as String)
            is Serializable -> bundle.putSerializable(key, value as Serializable)
            else -> {
                bundle.putSerializable(classKey, (value as Any)::class.java)
                bundle.putString(key, Gson().toJson(value))
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun restoreState(bundle: Bundle?) {
        if (bundle == null || !bundle.containsKey(key)) {
            value = defaultValue
            return
        }
        val nullableValue = when (value) {
            is Boolean -> bundle.getBoolean(key) as T
            is BooleanArray -> bundle.getBooleanArray(key) as T
            is Bundle -> bundle.getBundle(key) as T
            is Byte -> bundle.getByte(key) as T
            is ByteArray -> bundle.getByteArray(key) as T
            is Char -> bundle.getChar(key) as T
            is CharArray -> bundle.getCharArray(key) as T
            is Double -> bundle.getDouble(key) as T
            is DoubleArray -> bundle.getDoubleArray(key) as T
            is Float -> bundle.getFloat(key) as T
            is FloatArray -> bundle.getFloatArray(key) as T
            is Int -> bundle.getInt(key) as T
            is IntArray -> bundle.getIntArray(key) as T
            is Long -> bundle.getLong(key) as T
            is LongArray -> bundle.getLongArray(key) as T
            is Parcelable -> bundle.getParcelable<Parcelable>(key) as T
            is Short -> bundle.getShort(key) as T
            is ShortArray -> bundle.getShortArray(key) as T
            is String -> bundle.getString(key) as T
            is Serializable -> bundle.getSerializable(key) as T
            else -> {
                val klass = bundle.getSerializable(classKey) as Class<T>
                Gson().fromJson(bundle.getString(key), klass)
            }
        }
        value = nullableValue
    }

    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        return this.value
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        this.value = value
    }
}