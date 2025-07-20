package com.sleepkqq.sololeveling.gateway.model

data class TgAuthData(
	val tgWebAppData: TgWebAppData,
	val initData: String
) {

	constructor() : this(TgWebAppData(), "")
}
