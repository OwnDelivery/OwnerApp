package app.delivery.api.storage

import kotlinx.coroutines.flow.Flow

interface Storage<T> {

    fun update(data: T): Flow<Unit>

    fun get(): Flow<T?>
}