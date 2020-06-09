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

inline fun <reified R> func() {

}

interface Stateful {
    fun <T : Any> statefulProperty(defaultValue: T, key: String? = null): StatefulProperty<T>

    fun <T> statefulNullableProperty(defaultValue: T?, key: String? = null): StatefulProperty<T?>

    fun <V, T : MutableLiveData<V>> statefulLiveDataProperty(instance: T,
                                                             defaultValue: V?,
                                                             key: String? = null):
            StatefulProperty<T>

    fun restore(state: Bundle?)

    fun save(state: Bundle)
}

interface StatefulProperty<T> {
    operator fun provideDelegate(thisRef: Any, prop: KProperty<*>): ReadWriteProperty<Any, T>
}