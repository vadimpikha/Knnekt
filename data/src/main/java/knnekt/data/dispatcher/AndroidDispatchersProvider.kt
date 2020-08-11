package knnekt.data.dispatcher

import knnekt.domain.dispatcher.DispatchersProvider
import kotlinx.coroutines.Dispatchers

object AndroidDispatchersProvider: knnekt.domain.dispatcher.DispatchersProvider {
    override fun main() = Dispatchers.Main
    override fun io() = Dispatchers.IO
    override fun default() = Dispatchers.Default
}