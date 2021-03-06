package net.numalab.tetra

import net.kunmc.lab.configlib.BaseConfig
import net.kunmc.lab.configlib.value.BooleanValue
import net.kunmc.lab.configlib.value.DoubleValue
import net.kunmc.lab.configlib.value.EnumValue
import net.kunmc.lab.configlib.value.IntegerValue
import net.kunmc.lab.configlib.value.collection.StringListValue
import net.numalab.tetra.geo.FillAlgorithm
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.scoreboard.Team

class TetraConfig(plugin: Plugin) : BaseConfig(plugin) {
    private val joinedTeams = StringListValue(listOf())
    fun getJoinedTeams(): List<Team> {
        return joinedTeams.value().mapNotNull { Bukkit.getScoreboardManager().mainScoreboard.getTeam(it) }.distinct()
    }

    fun getJoinedPlayer(containNotTargetGameMode: Boolean): List<Player> {
        return if (containNotTargetGameMode) {
            getJoinedTeams().map { it.entries }.flatten().mapNotNull { Bukkit.getPlayer(it) }
        } else {
            getJoinedTeams().map { it.entries }.flatten().mapNotNull { Bukkit.getPlayer(it) }
                .filter { it.gameMode == targetGameMode.value() }
        }
    }

    fun clearJoinedTeam() {
        joinedTeams.clear()
    }

    fun addJoinedTeam(team: Team) {
        val added = (joinedTeams.value() + team.name).distinct()
        joinedTeams.value(added)
    }

    fun removeJoinedTeam(team: Team) {
        val removed = joinedTeams.value() - team.name
        joinedTeams.value(removed)
    }

    /**
     * 地面へのブロックの設置や、強制移動の切り替え
     */
    val isGoingOn = BooleanValue(false)

    /**
     * 強制移動の速度
     */
    val moveSpeed = DoubleValue(0.3)

    /**
     * 勝利判定時に自動的にOffにするかどうか
     */
    val isAutoOff = BooleanValue(true)

    val targetGameMode = EnumValue<GameMode>(GameMode.ADVENTURE)

    /**
     * 領土の値×この値 + moveSpeed = 強制移動の速度
     */
    val boostRate = DoubleValue(0.001)

    /**
     * 1tickあたりのブロック変更数
     */
    val maxBlockChangePerTick = IntegerValue(100)

    /**
     * 塗りつぶしアルゴリズムの切り替え
     */
    val fillAlgorithm = EnumValue<FillAlgorithm>(FillAlgorithm.FillFromOutSideOptimized)
}