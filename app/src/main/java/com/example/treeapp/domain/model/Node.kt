package com.example.treeapp.domain.model

import org.bouncycastle.jcajce.provider.digest.Keccak
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class Node(
    val id: Long,
    val name: String,
    val isRoot: Boolean,
) {

    companion object {
        @OptIn(ExperimentalUuidApi::class)
        fun create(parentId: Long?): Node {
            val random = Uuid.random()
            val name = getNameFromHash(generateKeccak256Hash(random.toString() + parentId))
            return Node(
                id = 0L,
                name = name,
                isRoot = parentId == null
            )
        }

        private fun generateKeccak256Hash(data: String): ByteArray {
            val digest = Keccak.Digest256()
            return digest.digest(data.toByteArray())
        }

        private fun getNameFromHash(hash: ByteArray): String {
            require(hash.size >= 20) { "Hash must be at least 20 bytes long" }

            val last20Bytes = hash.copyOfRange(hash.size - 20, hash.size)

            return "0x${last20Bytes.joinToString("") { "%02x".format(it) }}"
        }
    }
}


