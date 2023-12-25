package net.yogstation.yogbot.listeners.channel

import discord4j.common.util.Snowflake
import discord4j.core.`object`.reaction.ReactionEmoji
import discord4j.core.event.domain.message.MessageCreateEvent
import net.yogstation.yogbot.config.DiscordChannelsConfig
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class PublicChannel(channelsConfig: DiscordChannelsConfig) : AbstractChannel(channelsConfig) {
	override val channel: Snowflake = Snowflake.of(channelsConfig.channelPublic)
	override fun handle(event: MessageCreateEvent): Mono<*> {
		val message = event.message
		var message_content = message.getContent()
		if (!message_content.contains("hohoho", ignoreCase = true) && !message_content.contains("hohoho", ignoreCase = true)) {
			message.delete("You must be festive! Include "hohoho" or "ho ho ho" in your message for the christmas season!")
		}
	}
}
