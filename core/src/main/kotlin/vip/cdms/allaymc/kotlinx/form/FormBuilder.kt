package vip.cdms.allaymc.kotlinx.form

import org.allaymc.api.form.type.Form
import vip.cdms.allaymc.kotlinx.Player

abstract class FormBuilder<T : Form, R : FormBuilder.Response> {
    interface Response

    open var title: String = ""
    open var echo: Player.(R?) -> Unit = {}
    open var submit: Player.(R) -> Unit = {}
    open var close: Player.() -> Unit = {}

    internal open fun response(player: Player, response: R?) = with(player) {
        if (response != null) submit(response) else close()
        echo(response)
    }

    abstract fun build(player: Player): T
    infix fun sendTo(player: Player) = build(player).apply { sendTo(player) }
}

infix fun Player.send(builder: FormBuilder<*, *>) = builder sendTo this

fun ModalFormBuilder(block: ModalFormBuilder.() -> Unit) = ModalFormBuilder().apply(block)
fun SimpleFormBuilder(block: SimpleFormBuilder.() -> Unit) = SimpleFormBuilder().apply(block)
fun CustomFormBuilder(block: CustomFormBuilder.() -> Unit) = CustomFormBuilder().apply(block)

operator fun SimpleFormBuilder.plus(other: SimpleFormBuilder.() -> Unit) = plus(SimpleFormBuilder(other))
operator fun SimpleFormBuilder.plus(other: SimpleFormBuilder) = object : SimpleFormBuilder() {
    val origin = this@SimpleFormBuilder
    val originSize = origin.buttons.size
    override var title = other.title.ifBlank { origin.title }
    override var content = other.content.ifBlank { origin.content }
    override val buttons = (origin.buttons + other.buttons).toMutableList()
    override val callbacks = (origin.callbacks + other.callbacks).toMutableMap()
    override fun response(player: Player, response: Response?) {
        super.response(player, response)
        if (response == null || response.index < originSize)
            origin.response(player, response)
        if (response == null || response.index >= originSize)
            origin.response(player, response?.let { Response(it.index - originSize) })
    }
}

operator fun CustomFormBuilder.plus(other: CustomFormBuilder.() -> Unit) = plus(CustomFormBuilder(other))
operator fun CustomFormBuilder.plus(other: CustomFormBuilder) = object : CustomFormBuilder() {
    val origin = this@CustomFormBuilder
    val originSize = origin.elements.size
    val otherSize = other.elements.size
    override var title = other.title.ifBlank { origin.title }
    override var icon = other.icon ?: origin.icon
    override val elements = (origin.elements + other.elements).toMutableList()
    override fun response(player: Player, response: Response?) {
        super.response(player, response)
        origin.response(player, response?.let { it.copy(values = it.values.dropLast(otherSize)) })
        other.response(player, response?.let { it.copy(values = it.values.drop(originSize)) })
    }
}
