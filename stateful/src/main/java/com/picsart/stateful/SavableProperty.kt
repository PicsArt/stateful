/*
 * Copyright (C) 2018 PicsArt, Inc.
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

class SavableProperty<T : Any>(
        private val defaultValue: T,
        private val key: String) :
        ReadWriteProperty<Any, T> {

    private var value: T = defaultValue

    fun saveState(bundle: Bundle) {
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
            else -> bundle.putString(key, Gson().toJson(value))
        }
    }

    fun restoreState(bundle: Bundle) {
        val nullableValue: T? = when (value) {
            is Boolean -> bundle.getBoolean(key, defaultValue as Boolean) as T?
            is BooleanArray -> bundle.getBooleanArray(key) as T?
            is Bundle -> bundle.getBundle(key) as T?
            is Byte -> bundle.getByte(key, defaultValue as Byte) as T?
            is ByteArray -> bundle.getByteArray(key) as T?
            is Char -> bundle.getChar(key, defaultValue as Char) as T?
            is CharArray -> bundle.getCharArray(key) as T?
            is Double -> bundle.getDouble(key, defaultValue as Double) as T?
            is DoubleArray -> bundle.getDoubleArray(key) as T?
            is Float -> bundle.getFloat(key, defaultValue as Float) as T?
            is FloatArray -> bundle.getFloatArray(key) as T?
            is Int -> bundle.getInt(key, defaultValue as Int) as T?
            is IntArray -> bundle.getIntArray(key) as T?
            is Long -> bundle.getLong(key, defaultValue as Long) as T?
            is LongArray -> bundle.getLongArray(key) as T?
            is Parcelable -> bundle.getParcelable<Parcelable>(key) as T?
            is Short -> bundle.getShort(key, defaultValue as Short) as T?
            is ShortArray -> bundle.getShortArray(key) as T?
            is String -> bundle.getString(key, defaultValue as String) as T?
            is Serializable -> bundle.getSerializable(key) as T?
            else -> Gson().fromJson(bundle.getString(key), value::class.java)
        }
        value = nullableValue ?: defaultValue
    }

    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        return this.value
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        this.value = value
    }
}