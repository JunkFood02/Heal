package io.github.junkfood.heal.ui.common

object NavigationGraph {
    const val FEED = "feed"
    const val EPISODE = "episode"
    const val PODCAST = "podcast"
    const val LIBRARY = "library"
    const val SETTINGS = "settings"
    const val LISTEN = "listen"
    const val SUBSCRIPTIONS = "subscriptions"

    fun String.withArgument(argumentName: String): String = "$this/{$argumentName}"

    fun String.toId(Id: Long): String = "$this/$Id"

    const val EPISODE_ID = "episode_id"
    const val PODCAST_ID = "podcast_id"
}