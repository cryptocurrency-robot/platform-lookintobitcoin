package org.freekode.cryptobot.platformlookintobitcoin.infrastructure.lookintobitcoin

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.TreeNode
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.boot.jackson.JsonComponent
import org.springframework.stereotype.Service


@JsonComponent
@Service
class LookintobitcoinResponseDeserializer : JsonDeserializer<LookintobitcoinResponseDTO>() {
    private val objectMapper = ObjectMapper().registerModule(KotlinModule.Builder().build())

    override fun deserialize(parser: JsonParser, context: DeserializationContext): LookintobitcoinResponseDTO {
        val tree: TreeNode = parser.codec.readTree(parser)
        val lineNodes: ArrayNode = tree["response"]["props"]["figure"]["data"] as ArrayNode

        val lines = lineNodes
            .map { objectMapper.treeToValue(it, LookintobitcoinLineDTO::class.java) }
            .toList()
        return LookintobitcoinResponseDTO(lines)
    }
}
