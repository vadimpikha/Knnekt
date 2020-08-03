package blasty.data.dispatcher

import blasty.domain.dispatcher.DispatchersProvider
import kotlinx.coroutines.Dispatchers

object AndroidDispatchersProvider: DispatchersProvider {
    override fun main() = Dispatchers.Main
    override fun io() = Dispatchers.IO
    override fun default() = Dispatchers.Default
}