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
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class StatefulImplTest : Stateful by StatefulImpl() {

    data class A(val a: Int)

    enum class B {
        B_1, B_2
    }

    private var testProperty by statefulProperty(3)

    private var testNullableProperty by statefulNullableProperty<Int>(null)

    private var testNullableProperty1 by statefulNullableProperty(false)

    private var testNullableProperty2 by statefulNullableProperty<A>(null)

    private var testNullableProperty3 by statefulNullableProperty(A(2))

    private var testNullableProperty4 by statefulNullableProperty("1234")

    private var testNullableProperty5 by statefulNullableProperty(B.B_2)

    private var testPropertyWithKey by statefulProperty(-78, "test property")

    private var liveData by statefulLiveDataProperty(MutableLiveData<Int>(), null, "test livedata property")

    @Test
    fun propertyLiveDataTest() {
        Assert.assertEquals(null, liveData.value)
        var isFirst = false
        var observer = Observer<Int> {
            if (!isFirst) {
                Assert.assertEquals(null, it)
                isFirst = true
            } else {
                Assert.assertEquals(90, it)
            }

        }
        liveData.observeForever(observer)
        liveData.apply { value = 90 }
        Assert.assertEquals(liveData.value, 90)

        val bundle = Bundle()
        liveData.removeObserver(observer)
        saveInternal(bundle)
        isFirst = false
        observer = Observer {
            if (!isFirst) {
                Assert.assertEquals(90, it)
                isFirst = true
            } else {
                Assert.assertEquals(128, it)
            }
        }
        restore(bundle)
        liveData.observeForever(observer)
        liveData.apply { value = 128 }
        Assert.assertEquals(liveData.value, 128)
        liveData.removeObserver(observer)

        isFirst = false
        observer = Observer {
            if (!isFirst) {
                Assert.assertEquals(128, it)
                isFirst = true
            } else {
                Assert.assertEquals(90, it)
            }
        }
        liveData.observeForever(observer)
        restore(bundle)
        Assert.assertEquals(liveData.value, 90)
    }

    @Test
    fun propertyTest() {
        Assert.assertEquals(3, testProperty)
        testProperty = 9
        Assert.assertEquals(9, testProperty)
        val bundle = Bundle()
        saveInternal(bundle)
        Assert.assertTrue(bundle.containsKey("testProperty"))
        Assert.assertEquals(9, bundle.get("testProperty"))
        testProperty = 67
        Assert.assertEquals(67, testProperty)
        restore(null)
        restore(bundle)
        Assert.assertEquals(9, testProperty)
    }

    @Test
    fun propertyNullableTest() {
        Assert.assertEquals(null, testNullableProperty)
        testNullableProperty = 9
        Assert.assertEquals(9, testNullableProperty)
        testNullableProperty = null
        Assert.assertEquals(null, testNullableProperty)

        var bundle = Bundle()
        saveInternal(bundle)
        Assert.assertTrue(!bundle.containsKey("testNullableProperty"))
        Assert.assertEquals(null, bundle.get("testNullableProperty"))
        testNullableProperty = 67
        Assert.assertEquals(67, testNullableProperty)
        restore(bundle)
        Assert.assertTrue(!bundle.containsKey("testNullableProperty"))
        Assert.assertEquals(null, testNullableProperty)
        Assert.assertTrue(!bundle.containsKey("testNullableProperty"))
        Assert.assertEquals(null, testNullableProperty)

        testNullableProperty = 67
        bundle = Bundle()
        saveInternal(bundle)
        testNullableProperty = null
        Assert.assertTrue(bundle.containsKey("testNullableProperty"))
        Assert.assertEquals(67, bundle.get("testNullableProperty"))
        restore(bundle)
        Assert.assertEquals(67, testNullableProperty)

    }

    @Test
    fun propertyWithKeyTest() {
        Assert.assertEquals(-78, testPropertyWithKey)
        testPropertyWithKey = 9
        Assert.assertEquals(9, testPropertyWithKey)
        val bundle = Bundle()
        saveInternal(bundle)
        Assert.assertTrue(bundle.containsKey("test property"))
        Assert.assertEquals(9, bundle.get("test property"))
        testPropertyWithKey = 67
        Assert.assertEquals(67, testPropertyWithKey)
        restore(null)
        restore(bundle)
        Assert.assertEquals(9, testPropertyWithKey)
    }

    //in android, after save state properties are likely to be cleared (i.e. low memory)
    private fun saveInternal(bundle: Bundle) {
        save(bundle)
        testNullableProperty = null
        testNullableProperty1 = null
        testNullableProperty2 = null
        testNullableProperty3 = null
        testNullableProperty4 = null
        testNullableProperty5 = null
        liveData.value = null
    }
}