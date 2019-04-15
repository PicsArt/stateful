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
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.*
import kotlin.math.abs
import kotlin.reflect.KClass

//TODO implement tests for types Bundle, Parcelable, Serializable
@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class SavablePropertyTest {
    @Test
    fun booleanTest() {
        testProperty(Boolean::class)
    }

    @Test
    fun booleanArrayTest() {
        testProperty(BooleanArray::class)
    }

    @Test
    fun byteTest() {
        testProperty(Byte::class)
    }

    @Test
    fun byteArrayTest() {
        testProperty(ByteArray::class)
    }


    @Test
    fun charTest() {
        testProperty(Char::class)
    }

    @Test
    fun charArrayTest() {
        testProperty(CharArray::class)
    }

    @Test
    fun doubleTest() {
        testProperty(Double::class)
    }

    @Test
    fun doubleArrayTest() {
        testProperty(DoubleArray::class)
    }

    @Test
    fun floatTest() {
        testProperty(Float::class)
    }

    @Test
    fun floatArrayTest() {
        testProperty(FloatArray::class)
    }

    @Test
    fun intTest() {
        testProperty(Int::class)
    }

    @Test
    fun intArrayTest() {
        testProperty(IntArray::class)
    }

    @Test
    fun enumTest() {
        testProperty(Mode::class)
    }

    @Test
    fun longTest() {
        testProperty(Long::class)
    }

    @Test
    fun longArrayTest() {
        testProperty(LongArray::class)
    }

    @Test
    fun shortTest() {
        testProperty(Short::class)
    }

    @Test
    fun shortArrayTest() {
        testProperty(ShortArray::class)
    }

    @Test
    fun stringTest() {
        testProperty(String::class)
    }


    private fun <T : Any> testProperty(type: KClass<T>) {
        var rand = generateRandomValueOfType(type)
        val name = type.simpleName!!
        var property = SavableProperty(rand, name)
        assertEquals(type, rand, property.get())
        rand = generateRandomValueOfType(type)
        property = SavableProperty(rand, name)
        val bundle = Bundle()
        property.saveState(bundle)
        Assert.assertTrue(bundle.containsKey(name))
        assertEquals(type, rand, property.get())
        val nextRand = generateRandomValueOfType(type)
        property.set(nextRand)
        assertEquals(type, nextRand, property.get())
        property.restoreState(bundle)
        assertEquals(type, rand, property.get())
        rand = generateRandomValueOfType(type)
        property.set(rand)
        assertEquals(type, rand, property.get())
        property.saveState(bundle)
        property = SavableProperty(generateRandomValueOfType(type), name)
        property.restoreState(bundle)
        assertEquals(type, rand, property.get())
    }

    private fun <T : Any> assertEquals(type: KClass<T>, expected: T, actual: T) {
        when (type) {
            BooleanArray::class -> Assert.assertArrayEquals(expected as BooleanArray, actual as BooleanArray)
            CharArray::class -> Assert.assertArrayEquals(expected as CharArray, actual as CharArray)
            DoubleArray::class -> Assert.assertArrayEquals(expected as DoubleArray, actual as DoubleArray, 0.0)
            FloatArray::class -> Assert.assertArrayEquals(expected as FloatArray, actual as FloatArray, 0f)
            IntArray::class -> Assert.assertArrayEquals(expected as IntArray, actual as IntArray)
            LongArray::class -> Assert.assertArrayEquals(expected as LongArray, actual as LongArray)
            ShortArray::class -> Assert.assertArrayEquals(expected as ShortArray, actual as ShortArray)
            Array<out Any>::class -> Assert.assertArrayEquals(expected as Array<*>, actual as Array<*>)
            else -> Assert.assertEquals(expected, actual)
        }
    }

    private fun <T : Any> SavableProperty<T>.get(): T {
        return this.getValue(this, this::javaClass)
    }

    private fun <T : Any> SavableProperty<T>.set(value: T) {
        return this.setValue(this, this::javaClass, value)
    }

    private fun <T : Any> generateRandomValueOfType(type: KClass<T>): T {
        return when (type) {
            Boolean::class -> Random().nextBoolean() as T
            BooleanArray::class -> generateRandomArrayOfType(Boolean::class) as T
            Byte::class -> {
                val bytes = ByteArray(1)
                Random().nextBytes(bytes)
                bytes[0] as T
            }
            ByteArray::class -> {
                val bytes = ByteArray((abs(generateRandomValueOfType(Int::class)) % 13) + 1)
                Random().nextBytes(bytes)
                bytes[0] as T
            }
            Char::class -> (Random().nextInt(26) + 'a'.toInt()).toChar() as T
            CharArray::class -> generateRandomArrayOfType(Char::class) as T
            Double::class -> Random().nextDouble() as T
            DoubleArray::class -> generateRandomArrayOfType(Double::class) as T
            Float::class -> Random().nextFloat() as T
            FloatArray::class -> generateRandomArrayOfType(Float::class) as T
            Int::class -> Random().nextInt() as T
            IntArray::class -> generateRandomArrayOfType(Int::class) as T
            Long::class -> Random().nextLong() as T
            LongArray::class -> generateRandomArrayOfType(Long::class) as T
            Short::class -> Random().nextInt(Short.MAX_VALUE + 1).toShort() as T
            ShortArray::class -> generateRandomArrayOfType(Short::class) as T
            String::class -> UUID.randomUUID().toString() as T
            Mode::class -> Mode.values()[Random().nextInt(Short.MAX_VALUE + 1) % 2] as T
            else -> {
                throw TypeNotPresentException("generateRandomValueOfType is not implemented for $type", null)
            }
        }
    }

    private fun <T : Any> generateRandomArrayOfType(type: KClass<T>): T {
        var size = 0
        while (size <= 0) {
            size = (abs(generateRandomValueOfType(Int::class)) % 13) + 1
        }
        return when (type) {
            Boolean::class -> BooleanArray(size) { generateRandomValueOfType(type) as Boolean } as T
            Char::class -> CharArray(size) { generateRandomValueOfType(type) as Char } as T
            Double::class -> DoubleArray(size) { generateRandomValueOfType(type) as Double } as T
            Float::class -> FloatArray(size) { generateRandomValueOfType(type) as Float } as T
            Int::class -> IntArray(size) { generateRandomValueOfType(type) as Int } as T
            Long::class -> LongArray(size) { generateRandomValueOfType(type) as Long } as T
            Short::class -> ShortArray(size) { generateRandomValueOfType(type) as Short } as T
            else -> Array<Any>(size) { generateRandomValueOfType(type) } as T
        }
    }
}

enum class Mode {
    DEFAULT,
    OTHER
}