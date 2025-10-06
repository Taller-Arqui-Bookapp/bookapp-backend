package com.bookapp.backend.adapters.out.googlebooks

import com.bookapp.backend.domain.ports.BookInfoPort
import com.bookapp.backend.domain.ports.ExternalBook
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono

@Component
class GoogleBooksAdapter(private val booksWebClient: WebClient) : BookInfoPort {

    override fun findByIsbn(isbn: String): ExternalBook? {
        val data: Map<String, Any> = booksWebClient.get()
            .uri("/volumes?q=isbn:{isbn}", isbn)
            .retrieve()
            .bodyToMono<Map<String, Any>>()
            .onErrorReturn(emptyMap())
            .block() ?: return null

        val items = data["items"] as? List<*> ?: return null
        if (items.isEmpty()) return null

        return parseGoogleBookItem(items.first() as Map<*, *>, isbn)
    }

    override fun search(query: String, page: Int, size: Int): List<ExternalBook> {
        val startIndex = page * size
        val res: Map<String, Any> = booksWebClient.get()
            .uri { builder ->
                builder.path("/volumes")
                    .queryParam("q", query)
                    .queryParam("startIndex", startIndex)
                    .queryParam("maxResults", size)
                    .queryParam("printType", "books")
                    .build()
            }
            .retrieve()
            .bodyToMono<Map<String, Any>>()
            .onErrorReturn(emptyMap())
            .block() ?: emptyMap()

        val items = res["items"] as? List<*> ?: emptyList<Any>()
        return items.mapNotNull { item ->
            parseGoogleBookItem(item as? Map<*, *> ?: return@mapNotNull null)
        }
    }

    private fun parseGoogleBookItem(item: Map<*, *>, fallbackIsbn: String? = null): ExternalBook? {
        val volumeInfo = item["volumeInfo"] as? Map<*, *> ?: return null
        
        val title = volumeInfo["title"] as? String ?: return null
        val authors = (volumeInfo["authors"] as? List<*>)?.mapNotNull { it as? String } ?: emptyList()
        
        // Buscar ISBN-13 primero, luego ISBN-10
        val industryIdentifiers = volumeInfo["industryIdentifiers"] as? List<*>
        val isbn = industryIdentifiers?.let { identifiers ->
            identifiers.mapNotNull { id ->
                val idMap = id as? Map<*, *> ?: return@mapNotNull null
                val type = idMap["type"] as? String
                val identifier = idMap["identifier"] as? String
                when (type) {
                    "ISBN_13" -> identifier
                    "ISBN_10" -> identifier
                    else -> null
                }
            }.firstOrNull { it?.length == 13 } // Preferir ISBN-13
                ?: identifiers.mapNotNull { id ->
                    val idMap = id as? Map<*, *> ?: return@mapNotNull null
                    val type = idMap["type"] as? String
                    val identifier = idMap["identifier"] as? String
                    if (type == "ISBN_10") identifier else null
                }.firstOrNull()
        } ?: fallbackIsbn

        val publishedDate = volumeInfo["publishedDate"] as? String
        val year = publishedDate?.let { date ->
            // Extraer año de formatos como "2007", "2007-07", "2007-07-30"
            date.split("-").firstOrNull()?.toIntOrNull()
        }

        val imageLinks = volumeInfo["imageLinks"] as? Map<*, *>
        val coverUrl = imageLinks?.let { links ->
            // Preferir thumbnail de alta calidad
            (links["large"] as? String) 
                ?: (links["medium"] as? String)
                ?: (links["thumbnail"] as? String)
                ?: (links["smallThumbnail"] as? String)
        }?.replace("http://", "https://") // Asegurar HTTPS

        return ExternalBook(title, authors, isbn, year, coverUrl)
    }
}