package app.delivery.api.storage

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class InMemoryStorage<T> : Storage<T> {
    private val stateFlow = MutableStateFlow<T?>(null)

    override fun update(data: T): Flow<Unit> = flow<Unit> {
        stateFlow.emit(data)
    }.flowOn(Dispatchers.Default)

    override fun get(): Flow<T?> {
        return stateFlow
    }
}