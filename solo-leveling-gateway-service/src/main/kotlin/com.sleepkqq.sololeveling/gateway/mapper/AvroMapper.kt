package com.sleepkqq.sololeveling.gateway.mapper

import com.sleepkqq.sololeveling.avro.notification.ReceiveNotificationEvent
import com.sleepkqq.sololeveling.gateway.dto.WsUserNotification
import org.mapstruct.CollectionMappingStrategy
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.NullValueCheckStrategy
import org.mapstruct.NullValueMappingStrategy
import org.mapstruct.NullValuePropertyMappingStrategy
import org.mapstruct.ReportingPolicy

@Mapper(
	componentModel = "spring",
	unmappedTargetPolicy = ReportingPolicy.IGNORE,
	collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
	nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
	nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_DEFAULT,
	nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
abstract class AvroMapper {

	@Mapping(target = "message", source = "input.notification.message")
	@Mapping(target = "notificationType", source = "input.notification.type")
	abstract fun map(input: ReceiveNotificationEvent): WsUserNotification
}