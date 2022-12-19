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
import android.widget.Toast
import app.keyboardly.lib.reflector.DynamicFeature
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.android.play.core.splitinstall.SplitInstallStateUpdatedListener
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import timber.log.Timber

/**
 * The ViewModel for our single screen in the app.
 *
 * This is shared across the 3 flavors,
 * that's why we have to keep it clear of any Dagger bits for simplicity.
 *
 * It needs subclassing to override the initializeStorageFeature() method,
 * according to the chosen flavor's dynamic code loading mechanism.
 */
abstract class ModuleDownloader(
    val context: Context,
    var callback: InstallFeatureCallback?
) {

    val splitInstallManager = SplitInstallManagerFactory.create(context)
    private val TAG = "SplitInstallMgrFactory"
    private var sessionId = 0

    var dynamicModule: DynamicFeature? = null

    var featureName: String = ""

    fun removeCallback() {
        callback = null
    }

    fun setCallBack(callback: InstallFeatureCallback){
        this.callback = callback
    }

    private val listener = SplitInstallStateUpdatedListener { state ->
        if (state.sessionId() == sessionId) {
            when (state.status()) {
                SplitInstallSessionStatus.FAILED -> {
                    val message = "Install failed with error code ${state.errorCode()}"
                    showToast(context, message)
                    callback?.onError(message)
                }
                SplitInstallSessionStatus.DOWNLOADING -> {
                }
                SplitInstallSessionStatus.CANCELED -> {
                    val message = "Install canceled."
                    showToast(context, message)
                }
                SplitInstallSessionStatus.CANCELING -> {
                    val message = "Canceling install.."
                    showToast(context, message)
                }
                SplitInstallSessionStatus.PENDING -> {}
                SplitInstallSessionStatus.INSTALLING -> {}
                SplitInstallSessionStatus.INSTALLED -> {
                    val modules = state.moduleNames()
                    Timber.i("module=$modules")
                    callback?.onSuccess(modules)
                    /*showToast(
                        context, "Feature $featureName installed successfully"
                    )*/
                }
                else -> {
                    Timber.d("Status: ${state.status()}")
                }
            }
        }
    }

    private fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    init {
        splitInstallManager.registerListener(listener)
    }

    fun isFeatureInstalled(featureName: String) =
        splitInstallManager.installedModules.contains(featureName)

    fun requestInstall(moduleName: String, name: String) {
        featureName = name
        callback?.onLoading(true)
        val request = SplitInstallRequest
            .newBuilder()
            .addModule(moduleName)
            .build()

        splitInstallManager
            .startInstall(request)
            .addOnSuccessListener { id -> sessionId = id }
            .addOnFailureListener { exception ->
                Timber.e(exception, "Error installing module: ")
                callback?.onLoading(false)
                callback?.onError(exception.message)
            }
    }

    fun deleteFeature(featureName: String) {
        splitInstallManager.deferredUninstall(listOf(featureName))
    }
}

interface InstallFeatureCallback {
    fun onLoading(loading: Boolean)
    fun onError(message: String?)
    fun onSuccess(modules: List<String>)
}
