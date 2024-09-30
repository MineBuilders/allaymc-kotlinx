package vip.cdms.allaymc.kotlinx

import org.allaymc.api.plugin.Plugin

@Suppress("unused")
class KotlinxPlugin : Plugin() {
    override fun onLoad() = pluginLogger.info("KotlinPluginTemplate loaded!")
    override fun onEnable() = pluginLogger.info("KotlinPluginTemplate enabled!")
    override fun onDisable() = pluginLogger.info("KotlinPluginTemplate disabled!")
}
