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
package app.keyboardly.dev.keyboard.utils

import android.content.Context
import app.keyboardly.lib.reflector.DynamicFeature
import app.keyboardly.dev.keyboard.keypad.KokoKeyboardView

class DynamicModuleHelper(
    context: Context,
    callback: InstallFeatureCallback?
) : ModuleDownloader(context, callback) {

    fun initialize(featureNameId: String): DynamicFeature? {
        KokoKeyboardView.rebuildBaseComponent(featureNameId)
        dynamicModule = KokoKeyboardView.component.dynamicFeature()
        if (dynamicModule != null) {
            return dynamicModule
        }
        return null
    }

}
