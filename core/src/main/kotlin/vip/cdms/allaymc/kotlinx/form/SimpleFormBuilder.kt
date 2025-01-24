package vip.cdms.allaymc.kotlinx.form

import org.allaymc.api.form.Forms
import org.allaymc.api.form.element.Button
import org.allaymc.api.form.type.SimpleForm
import vip.cdms.allaymc.kotlinx.Player

open class SimpleFormBuilder : FormBuilder<SimpleForm, SimpleFormBuilder.Response>(), FormImager {
    open var content = ""

    data class Button(val text: String, val image: FormImage? = null)
    fun Button.convert() = Button(text, image?.convert())

    open val buttons = mutableListOf<Button>()
    open val callbacks = mutableMapOf<Button, MutableList<(Player.() -> Unit)?>>()

    fun buttonOf(text: String, image: FormImage? = null, callback: (Player.() -> Unit)? = null) =
        Button(text, image).apply { callbacks.getOrPut(this) { mutableListOf() } += callback }
    fun button(text: String, image: FormImage? = null, callback: (Player.() -> Unit)? = null) =
        buttonOf(text, image, callback).also { buttons += it }

    @JvmInline
    value class Response(val index: Int) : FormBuilder.Response

    override fun build(player: Player): SimpleForm = Forms.simple()
        .title(title)
        .content(content)
        .apply {
            this@SimpleFormBuilder.buttons.forEachIndexed { index, it ->
                val button = it.convert()
                button.onClick { _ ->
                    callbacks[it]?.forEach { it?.invoke(player) }
                    val response = Response(index)
                    response(player, response)
                }
                button(button)
            }
        }
        .onClose(Runnable { response(player, null) })
}
