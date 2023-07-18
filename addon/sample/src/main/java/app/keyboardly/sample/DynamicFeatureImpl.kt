/*
 * Copyright 2019 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package app.keyboardly.sample

import android.view.View
import app.keyboardly.sample.di.DaggerDynamicComponent
import app.keyboardly.lib.navigation.NavigationMenuModel
import app.keyboardly.lib.reflector.DynamicFeature
import app.keyboardly.sample.action.top.TopActionView
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class DynamicFeatureImpl @Inject constructor(
    private val sampleView: SampleDefaultClass,
) : DynamicFeature {

    /**
     * The provider singleton. It is accessed from the base app component to create the feature graph
     * using the provided dependencies, and return the feature instance.
     * */
    companion object Provider : DynamicFeature.Provider {
        override fun get(dependencies: DynamicFeature.Dependencies): DynamicFeature {
            return DaggerDynamicComponent.builder()
                .dependencies(dependencies)
                .build()
                .dynamicFeature()
        }
    }

    /**
     * get the default view of add on.
     * it's possible to be set to null, but don't empty View()
     */
    override fun getView(): View? {
        return sampleView.getView()
    }

    override fun getTopView(): View? = null //TopActionView(sampleView.dependency).getView()

    /**
     * submenu of add on, list of NavigationMenuModel
     * it's possible to set to empty mutableList() but getView() not null
     */
    override fun getSubMenus(): MutableList<NavigationMenuModel> {
        return sampleView.getSubmenus()
    }
}
