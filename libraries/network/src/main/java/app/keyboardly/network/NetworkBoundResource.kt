package app.keyboardly.network

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import kotlin.coroutines.coroutineContext

abstract class NetworkBoundResource<ResultType, RequestType> {
    private val result = MutableLiveData<Resource<ResultType>>()
    private val supervisorJob = SupervisorJob()

    suspend fun build(): NetworkBoundResource<ResultType, RequestType> {
        withContext(Dispatchers.Main) {
            result.value =
                Resource.loading()
        }
        CoroutineScope(coroutineContext).launch(supervisorJob) {
            val dbResult = loadFromDb()
            if (shouldFetch(dbResult)) {
                try {
                    fetchFromNetwork()
                } catch (e: Exception) {
                    e.printStackTrace()
                    setValue(Resource.error(e.message.toString()))
                }
            } else {
                setValue(Resource.success(dbResult))
            }
        }
        return this
    }

    fun asLiveData() = result as LiveData<Resource<ResultType>>

    private suspend fun fetchFromNetwork() {
//        Timber.d( "Fetch data from network")
        setValue(Resource.loading()) // Dispatch latest value quickly (UX purpose)
        val apiResponse = createCallAsync()
//        Timber.e( "Data fetched from network")
        saveCallResult(processResponse(apiResponse))
        setValue(Resource.success(processResponse(apiResponse)))
    }

    @MainThread
    private fun setValue(newValue: Resource<ResultType>) {
        if (result.value != newValue) result.postValue(newValue)
    }

    // Called to get the cached data from the database.
    @MainThread
    protected abstract suspend fun loadFromDb(): ResultType

    // Called with the data in the database to decide whether to fetch
    // potentially updated data from the network.
    @MainThread
    protected abstract fun shouldFetch(data: ResultType?): Boolean

    // Called to create the API call.
    @MainThread
    protected abstract suspend fun createCallAsync(): RequestType

    @WorkerThread
    protected abstract fun processResponse(response: RequestType): ResultType

    // Called to save the result of the API response into the database
    @WorkerThread
    protected abstract suspend fun saveCallResult(item: ResultType)
}