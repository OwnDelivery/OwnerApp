package app.delivery.api.proto

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import androidx.datastore.preferences.protobuf.InvalidProtocolBufferException
import app.delivery.api.protos.AddressProto
import java.io.InputStream
import java.io.OutputStream

object AddressProtoSerializer : Serializer<AddressProto> {
    override val defaultValue: AddressProto = AddressProto.getDefaultInstance()

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun readFrom(input: InputStream): AddressProto {
        try {
            return AddressProto.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun writeTo(t: AddressProto, output: OutputStream) = t.writeTo(output)
}