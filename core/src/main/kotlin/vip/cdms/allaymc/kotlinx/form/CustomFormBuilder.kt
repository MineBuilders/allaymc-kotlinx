package vip.cdms.allaymc.kotlinx.form

import org.allaymc.api.form.Forms
import org.allaymc.api.form.element.CustomFormElement
import org.allaymc.api.form.element.Dropdown
import org.allaymc.api.form.element.Input
import org.allaymc.api.form.element.Label
import org.allaymc.api.form.element.Slider
import org.allaymc.api.form.element.StepSlider
import org.allaymc.api.form.element.Toggle
import org.allaymc.api.form.type.CustomForm
import vip.cdms.allaymc.kotlinx.Player
import kotlin.reflect.KProperty

open class CustomFormBuilder : FormBuilder<CustomForm, CustomFormBuilder.Response>(), FormImager {
    open var icon: FormImage? = null

    class Element<T>(
        val converter: (String) -> T,
        val builder: () -> CustomFormElement,
    ) {
        lateinit var response: String
        val value get() = converter(response)
        operator fun getValue(thisRef: Any?, property: KProperty<*>) = value
    }

    open val elements = mutableListOf<Element<*>>()

    fun label(text: String) =
        Element({ null }) { Label(text) }
        .also { elements += it }
    fun input(text: String, placeholder: String = "", default: String = "") =
        Element({ it }) { Input(text, placeholder, default) }
        .also { elements += it }
    fun toggle(text: String, default: Boolean = false) =
        Element({ it.toBoolean() }) { Toggle(text, default) }
        .also { elements += it }
    fun dropdown(text: String, options: List<String>, default: Int = 0) =
        Element({ it.toInt() }) { Dropdown(text, options, default) }
        .also { elements += it }
    fun slider(text: String, min: Float, max: Float, step: Int = 1, default: Float = min) =
        Element({ it.toFloat() }) { Slider(text, min, max, step, default) }
        .also { elements += it }
    fun stepSlider(text: String, steps: List<String>, default: Int = 0) =
        Element({ it.toInt() }) { StepSlider(text, steps, default) }
        .also { elements += it }

    data class Response(val values: List<String?>) : FormBuilder.Response

    override fun build(player: Player): CustomForm = Forms.custom()
        .title(title)
        .icon(icon?.convert())
        .apply {
            elements.forEach { element(it.builder()) }
        }
        .onResponse {
            elements.forEachIndexed { index, element ->
                element.response = it[index] ?: /*label only*/""
            }
            response(player, Response(it))
        }
        .onClose(Runnable { response(player, null) })
}
