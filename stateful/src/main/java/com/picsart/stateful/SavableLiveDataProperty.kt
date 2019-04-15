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

class SavableLiveDataProperty<V : Any, T : MutableLiveData<V>>(private var instance: T,
                                                               private val defaultValue: V,
                                                               private val key: String) :
        ReadWriteProperty<Any, T>, Savable {

    private var property: SavableProperty<V> = SavableProperty(defaultValue, key)

    override fun saveState(bundle: Bundle) {
        property.value = instance.value!!
        property.saveState(bundle)
    }

    override fun restoreState(bundle: Bundle?) {
        if (bundle == null) {
            instance.apply { value = defaultValue }
        } else {
            if (bundle.containsKey(key)) {
                property.restoreState(bundle)
                instance.value = property.value
            }
        }

    }

    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        return this.instance
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        this.instance = value
    }
}