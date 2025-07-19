package com.sleepkqq.sololeveling.gateway.extensions

import com.sleepkqq.sololeveling.gateway.service.JwtService
import io.jsonwebtoken.Claims

fun Claims.getUsername(): String? = get(JwtService.USERNAME_CLAIM, String::class.java)
fun Claims.getFirstName(): String? = get(JwtService.FIRST_NAME_CLAIM, String::class.java)
fun Claims.getLastName(): String? = get(JwtService.LAST_NAME_CLAIM, String::class.java)
fun Claims.getPhotoUrl(): String? = get(JwtService.PHOTO_URL_CLAIM, String::class.java)
fun Claims.getLanguageCode(): String? = get(JwtService.LANGUAGE_CODE_CLAIM, String::class.java)
