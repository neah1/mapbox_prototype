package com.example.mapboxprototype

import aws.sdk.kotlin.services.dynamodb.DynamoDbClient
import aws.sdk.kotlin.services.dynamodb.model.QueryRequest

actual class DatabaseClient {
    actual suspend fun fetchData(query: String, parameters: Map<String, Any>): List<Map<String, Any>> {
        val client = DynamoDbClient { region = "us-east-1" }
        val response = client.query(
            QueryRequest {
                tableName = "your_table_name"
                // Configure your query request here
            }
        )
        client.close()

        // Convert response to List<Map<String, Any>>
        return response.items?.map { item ->
            item.mapKeys { entry -> entry.key }.mapValues { entry -> entry.value.s.toString() }
        } ?: emptyList()
    }
}
