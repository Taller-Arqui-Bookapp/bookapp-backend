package com.bookapp.backend.config

import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebMvcConfig : WebMvcConfigurer {
    
    override fun configureContentNegotiation(configurer: ContentNegotiationConfigurer) {
        configurer
            .favorParameter(false)  // No usar parámetros en URL
            .favorPathExtension(false)  // No usar extensiones de archivo
            .ignoreAcceptHeader(false)  // Usar Accept header
            .defaultContentType(MediaType.APPLICATION_XML)  // XML por defecto
            .mediaType("xml", MediaType.APPLICATION_XML)
            .mediaType("json", MediaType.APPLICATION_JSON)
    }
}