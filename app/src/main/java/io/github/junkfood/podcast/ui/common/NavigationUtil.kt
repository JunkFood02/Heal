package io.github.junkfood.podcast.ui.common

object NavigationUtil {
    const val FEED = "feed"
    const val EPISODE = "episode"
    const val PODCAST = "podcast"
    const val LIBRARY = "library"
    const val SETTINGS = "settings"

    fun String.withArgument(argumentName: String): String = "$this/{$argumentName}"

    fun String.toId(Id: Long): String = "$this/$Id"

    const val EPISODE_ID = "episodeID"
}