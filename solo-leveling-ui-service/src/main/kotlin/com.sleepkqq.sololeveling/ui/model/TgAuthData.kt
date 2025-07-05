package com.sleepkqq.sololeveling.ui.model

data class TgAuthData(
	val tgWebAppData: TgWebAppData,
	val initData: String
) {

	constructor() : this(TgWebAppData(), "")
}
