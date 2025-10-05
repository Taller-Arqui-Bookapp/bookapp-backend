package com.bookapp.backend

import org.junit.jupiter.api.Test
import org.springframework.test.context.ActiveProfiles
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@ActiveProfiles("test")

class BookappBackendApplicationTests {

	@Test
	fun contextLoads() {
		   // Este test simplemente verifica que el contexto Spring arranca bien
	}

}
