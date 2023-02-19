package app.delivery.api.repositories

import app.delivery.api.dtos.OrderDto
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.database.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import timber.log.Timber

class OrderRepository(
    private val orderDatabaseRef: DatabaseReference,
    private val pastOrderDatabaseReference: DatabaseReference
) {

    fun updateOrder(orderId: String, orderDto: OrderDto): Flow<Result<Unit>> =
        callbackFlow {
            orderDatabaseRef.child(orderId).updateChildren(orderDto.toUpdateFieldsMap())
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        trySend(Result.success(Unit))
                    } else {
                        val exception = task.exception ?: Exception("Unable to update order")
                        FirebaseCrashlytics.getInstance().recordException(exception)
                        trySend(Result.failure(exception))
                    }
                }

            awaitClose()
        }

    fun getOrders(): Flow<Result<Pair<String, OrderDto?>>> = callbackFlow {

        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                parseAndSendOrder(snapshot)

            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                parseAndSendOrder(snapshot)
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                trySend(Result.success(Pair(snapshot.key ?: "", null)))
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(error: DatabaseError) {
                Timber.e(error.message)
                FirebaseCrashlytics.getInstance().recordException(error.toException())
                trySend(Result.failure(error.toException()))
            }

            private fun parseAndSendOrder(snapshot: DataSnapshot) {
                try {
                    val ordersDto = snapshot.getValue(OrderDto::class.java)
                    if (ordersDto != null) {
                        trySend(Result.success(Pair(snapshot.key ?: "", ordersDto)))
                    } else {
                        Timber.w("Unable to parse order dto")
                        val exception = Exception("Unable to parse order dto")
                        FirebaseCrashlytics.getInstance().recordException(exception)
                        trySend(Result.failure(exception))
                    }
                } catch (e: Exception) {
                    FirebaseCrashlytics.getInstance().recordException(e)
                    trySend(Result.failure(e))
                }
            }

        }

        orderDatabaseRef.addChildEventListener(childEventListener)

        awaitClose { orderDatabaseRef.removeEventListener(childEventListener) }
    }

    fun getPastOrders(): Flow<Result<List<OrderDto>>> = callbackFlow {

        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val orders = snapshot.children.mapNotNull {
                    try {
                        it.getValue(OrderDto::class.java)
                    } catch (e: Exception) {
                        FirebaseCrashlytics.getInstance().recordException(e)
                        null
                    }
                }
                trySend(Result.success(orders.toList()))
            }

            override fun onCancelled(error: DatabaseError) {
                FirebaseCrashlytics.getInstance().recordException(error.toException())
                trySend(Result.failure(error.toException()))
            }

        }

        pastOrderDatabaseReference.orderByChild("created_at").limitToLast(50)
            .addValueEventListener(valueEventListener)

        awaitClose { pastOrderDatabaseReference.removeEventListener(valueEventListener) }
    }
}