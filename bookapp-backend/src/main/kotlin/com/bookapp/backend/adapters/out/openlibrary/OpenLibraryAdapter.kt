package com.bookapp.backend.adapters.out.openlibrary

import com.bookapp.backend.domain.ports.BookInfoPort
import com.bookapp.backend.domain.ports.ExternalBook
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono

@Component
class OpenLibraryAdapter(private val booksWebClient: WebClient) : BookInfoPort {

    override fun findByIsbn(isbn: String): ExternalBook? {
        val data: Map<String, Any> = booksWebClient.get()
            .uri("/isbn/{isbn}.json", isbn)
            .retrieve()
            .bodyToMono<Map<String, Any>>()
            .onErrorReturn(emptyMap())
            .block() ?: return null

        val title = data["title"] as? String ?: return null
        val year  = (data["publish_date"] as? String)?.takeLast(4)?.toIntOrNull()
        val authors: List<String> =
            (data["authors"] as? List<*>)?.mapNotNull { (it as? Map<*, *>)?.get("name") as? String }.orEmpty()
        val coverId = (data["covers"] as? List<*>)?.firstOrNull()?.toString()
        val cover   = coverId?.let { "https://covers.openlibrary.org/b/id/${it}-L.jpg" }

        return ExternalBook(title, authors, isbn, year, cover)
    }

    override fun search(query: String, page: Int, size: Int): List<ExternalBook> {
        val res: Map<String, Any> = booksWebClient.get()
            .uri { b -> b.path("/search.json")
                .queryParam("q", query)
                .queryParam("page", page + 1)
                .queryParam("limit", size)
                .build()
            }
            .retrieve()
            .bodyToMono<Map<String, Any>>()
            .onErrorReturn(emptyMap())
            .block() ?: emptyMap()

        val docs = res["docs"] as? List<*> ?: emptyList<Any>()
        return docs.mapNotNull { d ->
            val m = d as? Map<*, *> ?: return@mapNotNull null
            val title = m["title"] as? String ?: return@mapNotNull null
            val authors = (m["author_name"] as? List<*>)?.mapNotNull { it as? String }.orEmpty()
            val isbn13 = (m["isbn"] as? List<*>)?.mapNotNull { it as? String }?.find { it.length == 13 }
            val year   = (m["first_publish_year"] as? Number)?.toInt()
            val coverId = (m["cover_i"] as? Number)?.toInt()
            val cover   = coverId?.let { "https://covers.openlibrary.org/b/id/${it}-L.jpg" }
            ExternalBook(title, authors, isbn13, year, cover)
        }
    }
}
