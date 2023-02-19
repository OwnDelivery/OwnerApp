package app.delivery.contract

import java.util.concurrent.atomic.AtomicBoolean

data class ViewEvent<T>(private val value: T) : SingleEvent<T>(value)

abstract class SingleEvent<T>(private val content: T) {

    private val isConsumed = AtomicBoolean(false)

    fun consume(action: (T) -> Unit) {
        if (!isConsumed.getAndSet(true)) {
            action.invoke(content)
        }
    }

    fun consume(): T? {
        if (!isConsumed.getAndSet(true)) {
            return content
        }
        return null
    }

    fun reset() {
        isConsumed.set(false)
    }

    val peekContent: T
        get() = content
}