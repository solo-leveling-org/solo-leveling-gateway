package com.soloist.gateway.client

import com.soloist.proto.media.AttachmentView
import com.soloist.proto.media.DownloadAttachmentRequest
import com.soloist.proto.media.DownloadResizedRequest
import com.soloist.proto.media.GetAttachmentRequest
import com.soloist.proto.media.MediaServiceGrpcKt
import org.springframework.stereotype.Service

@Service
class MediaClient(
	private val mediaStub: MediaServiceGrpcKt.MediaServiceCoroutineStub
) {

	suspend fun downloadAttachment(id: String): Triple<ByteArray, String, String> {
		val request = DownloadAttachmentRequest.newBuilder().setId(id).build()
		val response = mediaStub.downloadAttachment(request)
		return Triple(response.data.toByteArray(), response.contentType, response.filename)
	}

	suspend fun downloadResized(id: String, width: Int, height: Int): Triple<ByteArray, String, String> {
		val request = DownloadResizedRequest.newBuilder().setId(id).setWidth(width).setHeight(height).build()
		val response = mediaStub.downloadResized(request)
		return Triple(response.data.toByteArray(), response.contentType, response.filename)
	}

	suspend fun getAttachment(id: String): AttachmentView {
		val request = GetAttachmentRequest.newBuilder().setId(id).build()
		return mediaStub.getAttachment(request)
	}
}
