package vip.cdms.allaymc.kotlinx.form

import org.allaymc.api.form.type.Form
import vip.cdms.allaymc.kotlinx.Player

abstract class FormBuilder<T : Form, R : FormBuilder.Response> {
    interface Response

    var title: String = ""
    var echo: Player.(R?) -> Unit = {}
    var submit: Player.(R) -> Unit = {}
    var close: Player.() -> Unit = {}

    protected open fun Player.onResponse(response: R?) {}
    protected fun response(player: Player, response: R?) = with(player) {
        if (response != null) submit(response) else close()
        echo(response)
        onResponse(response)
    }

    abstract fun build(player: Player): T
    infix fun send(player: Player) = build(player).apply { sendTo(player) }
}

fun ModalFormBuilder(block: ModalFormBuilder.() -> Unit) = ModalFormBuilder().apply(block)
fun SimpleFormBuilder(block: SimpleFormBuilder.() -> Unit) = SimpleFormBuilder().apply(block)

infix fun Player.send(builder: FormBuilder<*, *>) = builder send this
