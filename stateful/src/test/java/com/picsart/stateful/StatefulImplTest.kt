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

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.os.Bundle
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class StatefulImplTest : Stateful by StatefulImpl() {

    private var testProperty by statefulProperty(3)
    private var testPropertyWithKey by statefulProperty(-78, "test property")

    private var liveData by statefulLiveDataProperty(MutableLiveData(), -78, "test property")

    @Test
    fun propertyLiveDataTest() {
        Assert.assertNull(liveData.value)

        restore(null)
        Assert.assertEquals(-78, liveData.value)
        var isFirst = false
        var observer = Observer<Int> {
            if (!isFirst) {
                Assert.assertEquals(-78, it)
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
        save(bundle)
        isFirst = false
        observer = Observer {
            if (!isFirst) {
                Assert.assertEquals(90, it)
                isFirst = true
            } else {
                Assert.assertEquals(128, it)
            }
        }
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
        save(bundle)
        Assert.assertTrue(bundle.containsKey("testProperty"))
        Assert.assertEquals(9, bundle.get("testProperty"))
        testProperty = 67
        Assert.assertEquals(67, testProperty)
        restore(null)
        restore(bundle)
        Assert.assertEquals(9, testProperty)
    }

    @Test
    fun propertyWithKeyTest() {
        Assert.assertEquals(-78, testPropertyWithKey)
        testPropertyWithKey = 9
        Assert.assertEquals(9, testPropertyWithKey)
        val bundle = Bundle()
        save(bundle)
        Assert.assertTrue(bundle.containsKey("test property"))
        Assert.assertEquals(9, bundle.get("test property"))
        testPropertyWithKey = 67
        Assert.assertEquals(67, testPropertyWithKey)
        restore(null)
        restore(bundle)
        Assert.assertEquals(9, testPropertyWithKey)
    }
}