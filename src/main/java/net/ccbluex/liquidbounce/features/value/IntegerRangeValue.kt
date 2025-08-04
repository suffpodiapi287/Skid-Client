/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 */
package net.ccbluex.liquidbounce.features.value;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.*;
import net.ccbluex.liquidbounce.features.value.Value;
import org.jetbrains.annotations.NotNull;

class IntegerRangeValue(
    name: String,
    minValue: Int,
    maxValue: Int,
    val minimum: Int = 0,
    val maximum: Int = Int.MAX_VALUE,
) : Value<IntRange>(
    name,
    IntRange(minValue.coerceIn(minimum, maximum), maxValue.coerceIn(minimum, maximum))
) {

    var minValue = minValue.coerceIn(minimum, maximum)
    var maxValue = maxValue.coerceIn(minimum, maximum)

    private val defaultMin = this.minValue
    private val defaultMax = this.maxValue

    fun isVisible(): Boolean = displayable

    fun setMin(value: Int) {
        minValue = value.coerceIn(minimum, maximum)
        if (minValue > maxValue) maxValue = minValue
    }

    fun setMax(value: Int) {
        maxValue = value.coerceIn(minimum, maximum)
        if (maxValue < minValue) minValue = maxValue
    }

    fun getRange(): IntRange = IntRange(minValue, maxValue)

    fun resetToDefaultRange() {
        minValue = defaultMin
        maxValue = defaultMax
    }

    override fun toJson(): JsonElement {
        val obj = JsonObject()
        obj.addProperty("min", minValue)
        obj.addProperty("max", maxValue)
        return obj
    }

    override fun fromJson(element: JsonElement) {
        if (element.isJsonObject) {
            val obj = element.asJsonObject
            setMin(obj.get("min")?.asInt ?: minValue)
            setMax(obj.get("max")?.asInt ?: maxValue)
        }
    }
}
