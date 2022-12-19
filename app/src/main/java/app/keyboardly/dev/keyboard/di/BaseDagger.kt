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
package app.keyboardly.dev.keyboard.di

import android.app.Application
import android.content.Context
import app.keyboardly.lib.KeyboardActionDependency
import app.keyboardly.lib.reflector.DynamicFeature
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import timber.log.Timber

@Component(modules = [BaseModule::class])
interface BaseComponent : DynamicFeature.Dependencies {

    fun dynamicFeature(): DynamicFeature?

    @Component.Builder
    interface Builder {
        @BindsInstance fun featureNameId(featureNameId: String): Builder
        @BindsInstance fun keyboardActionDependency(dependency: KeyboardActionDependency): Builder
        fun build(): BaseComponent
    }
}

@Module
object BaseModule {
    private var dynamicFeature: DynamicFeature? = null

    /**
     * This method will return null until the required on-demand feature is installed.
     * It will cache the value the first time a non-null value is returned.
     */
    @Provides
    @JvmStatic
    fun dynamicFeatureProvider(baseComponent: BaseComponent): DynamicFeature? {
        return try {
            // Get the instance of the DynamicFeature.Provider, pass it the BaseComponent which fulfills the
            // DynamicFeature.Dependencies contract, and get the DynamicFeature instance in return.
            val featurePackageId = baseComponent.getFeatureNameId()
            if (featurePackageId.isNotEmpty()) {
                val stringProvider = "DynamicFeatureImpl\$Provider"
                val dynamicProviderClass = "$featurePackageId.$stringProvider"
                val instance = Class.forName(dynamicProviderClass).kotlin.objectInstance
                if (instance != null) {
                    val provider = instance as DynamicFeature.Provider
                    provider.get(baseComponent)
                        .also { dynamicFeature = it }
                } else null
            } else {
                Timber.e( "featureName id empty. not found")
                null
            }
        } catch (e: ClassNotFoundException) {
            Timber.e( "Provider class not found")
            e.printStackTrace()
            null
        }
    }

    @Provides
    @JvmStatic
    fun appContextProvider(application: Application): Context = application
}
