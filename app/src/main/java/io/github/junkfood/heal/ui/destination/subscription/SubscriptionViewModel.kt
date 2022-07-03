package io.github.junkfood.heal.ui.destination.subscription

import androidx.lifecycle.ViewModel
import io.github.junkfood.heal.database.Repository

class SubscriptionViewModel : ViewModel() {
    val subscriptionFlow = Repository.getPodcastsWithEpisodes()
}