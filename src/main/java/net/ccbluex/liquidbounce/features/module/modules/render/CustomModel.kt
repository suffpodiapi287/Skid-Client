package net.ccbluex.liquidbounce.features.module.modules.render

import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.value.*

@ModuleInfo(name = "CustomModel", category = ModuleCategory.RENDER)
class CustomModel : Module(name = "CustomModel", category = ModuleCategory.RENDER) {
    val mode = ListValue("Mode", arrayOf("Imposter", "Rabbit", "Freddy", "Female", "Invisible"), "Female")
    val rotatePlayer = BoolValue("RotatePlayer", false)
    val breastPhysics = BoolValue("BreastPhysics", true)
    val breastGravity = FloatValue("BreastGravity", 0.1f, 0.1f, 0.5f)
    val breastBounce = FloatValue("BreastBounce", 0.6f, 0.1f, 1.0f)
    val breastRotation = FloatValue("BreastRotation", 0f, 0f, 10f)
    val breastNoArmor = BoolValue("NoRenderInArmor", false)
    
    override val tag: String
        get() = mode.get()
}