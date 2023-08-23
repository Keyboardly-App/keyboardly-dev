package app.keyboardly.network

/**
 * Created by zainal on 11/23/21 - 2:23 PM
 */
interface FetchPresenter<T> {
    fun onLoading()
    fun onError(message: String?)
    fun onSuccess(data: T? = null)
}