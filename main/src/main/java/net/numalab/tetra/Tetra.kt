package net.numalab.tetra

import dev.kotx.flylib.flyLib
import org.bukkit.plugin.java.JavaPlugin

class Tetra : JavaPlugin() {
    private val config = TetraConfig(this).also {
        it.saveConfigIfAbsent()
        it.loadConfig()
    }

    val command = TetraCommand(config)

    init {
        flyLib {
            command(command)
        }
    }

    override fun onEnable() {
        // Plugin startup logic
        val autoSetter = AutoSetter(this, config).also { command.autoSetter = it }
        val manager =
            BlockManager(config, this, autoSetter, WorldFiller(this, config)).also { command.blockManager = it }
        BlockBreakPrevent(this, config)
        JumpPrevent(this, config)
        ForceMover(this, config, manager)
    }

    override fun onDisable() {
        // Plugin shutdown logic
        config.saveConfigIfPresent()
    }
}