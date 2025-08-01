/*
 * FDPClient Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge by LiquidBounce.
 * https://github.com/SkidderMC/FDPClient/
 */
package net.ccbluex.liquidbounce.features.special

import com.jagrosh.discordipc.IPCClient
import com.jagrosh.discordipc.IPCListener
import com.jagrosh.discordipc.entities.RichPresence
import com.jagrosh.discordipc.entities.pipe.PipeStatus
import net.ccbluex.liquidbounce.FDPClient
import net.ccbluex.liquidbounce.features.module.modules.client.DiscordRPCModule
import net.ccbluex.liquidbounce.utils.*
import net.ccbluex.liquidbounce.utils.ClientUtils.mc
import org.json.JSONObject
import java.time.OffsetDateTime
import kotlin.concurrent.thread

object DiscordRPC {
    private val ipcClient = IPCClient(1400473578188705822)
    private val timestamp = OffsetDateTime.now()
    private var running = false
    private var fdpwebsite = "fdpinfo.github.io - "

    fun run() {
        ipcClient.setListener(object : IPCListener {
            override fun onReady(client: IPCClient?) {
                running = true
                thread {
                    while (running) {
                        update()
                        try {
                            Thread.sleep(1000L)
                        } catch (ignored: InterruptedException) {
                        }
                    }
                }
            }

            override fun onClose(client: IPCClient?, json: JSONObject?) {
                running = false
            }
        })

        try {
            ipcClient.connect()
        } catch (e: Exception) {
            ClientUtils.logError("DiscordRPC failed to start: ${e.message}")
        } catch (e: RuntimeException) {
            ClientUtils.logError("DiscordRPC failed to start: ${e.message}")
        }
    }

    private fun update() {
    try {
        val builder = RichPresence.Builder()
        val discordRPCModule = FDPClient.moduleManager[DiscordRPCModule::class.java]!!
        builder.setStartTimestamp(timestamp)
        builder.setLargeImage("fdpclient_", "FDPClient+++ v1.0.0")
        builder.setDetails("FDPClient+++ v1.0.0")

        ServerUtils.getRemoteIp().also {
            val str = buildString {
                if (discordRPCModule.showServerValue.get())
                    append("Server: $it\n")
                if (discordRPCModule.showNameValue.get())
                    append("IGN: ${mc.thePlayer?.name ?: mc.session.username}\n")
                if (discordRPCModule.showHealthValue.get())
                    append("HP: ${mc.thePlayer?.health?.toInt() ?: "N/A"}\n")
                if (discordRPCModule.showModuleValue.get()) {
                    val enabled = FDPClient.moduleManager.modules.count { it.state }
                    val total = FDPClient.moduleManager.modules.size
                    append("Enable: $enabled of $total Modules\n")
                }
                if (discordRPCModule.showOtherValue.get()) {
                    val time = if (mc.isSingleplayer) "SinglePlayer" else SessionUtils.getFormatSessionTime()
                    append("Time: $time\n")
                }
            }

            builder.setState(
                if (it.equals("Injecting", true)) "Injecting" else str.trim()
            )
        }

        if (ipcClient.status == PipeStatus.CONNECTED)
            ipcClient.sendRichPresence(builder.build())
        
    } catch (e: Exception) {
        ClientUtils.logError("DiscordRPC update failed: ${e.message}")
    }
}

    fun stop() {
        if (ipcClient.status == PipeStatus.CONNECTED) ipcClient.close()
    }
}
