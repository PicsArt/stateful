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
import android.os.Bundle
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

@Suppress("unused")
fun state(): Stateful {
    return StatefulImpl()
}

class StatefulImpl : Stateful {

    internal val properties = mutableListOf<Savable>()

    override fun <T : Any> statefulProperty(defaultValue: T, key: String?): StatefulProperty<T> {
        return SavablePropertyWrapper(defaultValue, key)
    }

    override fun <V : Any, T : MutableLiveData<V>> statefulLiveDataProperty(instance: T,
                                                                            defaultValue: V,
                                                                            key: String?):
            StatefulProperty<T> {
        return SavablePropertyLiveDataWrapper(instance, defaultValue, key)
    }

    override fun restore(state: Bundle?) {
        properties.forEach {
            it.restoreState(state)
        }
    }

    override fun save(state: Bundle) {
        properties.forEach {
            it.saveState(state)
        }
    }

    private inner class SavablePropertyWrapper<T : Any>(val defaultValue: T, val key: String?) :
            StatefulProperty<T> {
        override operator fun provideDelegate(thisRef: Any, prop: KProperty<*>):
                ReadWriteProperty<Any, T> {
            val savableProperty = SavableProperty(defaultValue, key ?: prop.name)
            properties.add(savableProperty)
            return savableProperty
        }
    }

    private inner class SavablePropertyLiveDataWrapper<V : Any, T : MutableLiveData<V>>(
            val instance: T, val defaultValue: V,
            val key: String?) :
            StatefulProperty<T> {

        override operator fun provideDelegate(thisRef: Any, prop: KProperty<*>):
                ReadWriteProperty<Any, T> {
            val savableProperty = SavableLiveDataProperty(instance, defaultValue,
                    key ?: prop.name)
            properties.add(savableProperty)
            return savableProperty
        }
    }
}