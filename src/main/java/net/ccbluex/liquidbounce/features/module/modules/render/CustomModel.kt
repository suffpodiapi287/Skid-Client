package net.aspw.client.features.module.impl.visual

import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.value.ListValue

@ModuleInfo(name = "CustomModel", category = ModuleCategory.RENDER)
class CustomModel : Module(name = "CustomModel", category = ModuleCategory.RENDER) {
    val mode = ListValue("Mode", arrayOf("Imposter", "Rabbit", "Freddy"), "Imposter")

    override val tag: String
        get() = mode.get()
}