package com.soloist.gateway.controller

import com.soloist.gateway.client.MediaClient
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/media")
class MediaController(
	private val mediaClient: MediaClient
) {

	@GetMapping("/{id}")
	suspend fun download(
		@PathVariable id: String,
		@RequestParam(required = false) width: Int?,
		@RequestParam(required = false) height: Int?
	): ResponseEntity<ByteArray> {
		val (data, contentType, filename) = if (width != null && height != null) {
			mediaClient.downloadResized(id, width, height)
		} else {
			mediaClient.downloadAttachment(id)
		}

		return ResponseEntity.ok()
			.contentType(MediaType.parseMediaType(contentType))
			.header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"$filename\"")
			.header(HttpHeaders.CONTENT_LENGTH, data.size.toString())
			.body(data)
	}
}
