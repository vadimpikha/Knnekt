package knnekt.shared.data.resources

import android.content.res.Resources

interface AppResources {

    fun getString(resId: Int): String

}

class AppResourcesImpl(
    private val resources: Resources
) : AppResources {

    override fun getString(resId: Int): String = resources.getString(resId)

}