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
import com.picsart.stateful.Stateful
import com.picsart.stateful.StatefulImpl
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