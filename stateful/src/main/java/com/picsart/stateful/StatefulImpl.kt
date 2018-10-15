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
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

fun state(): Stateful {
    return StatefulImpl()
}

class StatefulImpl : Stateful {

    private val properties = mutableListOf<SavableProperty<*>>()

    override fun <T : Any> statefulProperty(defaultValue: T, key: String?): StatefulProperty<T> {
        return SavablePropertyWrapper(defaultValue, key)
    }

    override fun restore(state: Bundle?) {
        if (state == null) {
            return
        }
        properties.forEach {
            it.restoreState(state)
        }
    }

    override fun save(state: Bundle) {
        properties.forEach {
            it.saveState(state)
        }
    }

    private inner class SavablePropertyWrapper<T : Any>(val defaultValue: T, val key: String?) : StatefulProperty<T> {
        override operator fun provideDelegate(thisRef: Any, prop: KProperty<*>): ReadWriteProperty<Any, T> {
            val savableProperty = SavableProperty(defaultValue, key ?: prop.name)
            properties.add(savableProperty)
            return savableProperty
        }
    }
}