package vip.cdms.allaymc.kotlinx.form

import org.allaymc.api.form.Forms
import org.allaymc.api.form.type.ModalForm
import vip.cdms.allaymc.kotlinx.Player
import vip.cdms.allaymc.kotlinx.Receiver

class ModalFormBuilder : FormBuilder<ModalForm, ModalFormBuilder.Response>(){
    var content = ""

    data class Action(val text: String, val callbacks: MutableList<Receiver<Player>?>)
    fun actionOf(text: String, callback: Receiver<Player>? = null) = Action(text, mutableListOf()).apply { callbacks += callback }
    var confirm: Action? = null
    var cancel: Action? = null

    fun confirm(text: String? = null, callback: Receiver<Player>? = null) {
        confirm = confirm?.copy(text = text ?: confirm!!.text) ?: actionOf(text ?: "")
        confirm!!.callbacks += callback
    }
    fun cancel(text: String? = null, callback: Receiver<Player>? = null) {
        cancel = cancel?.copy(text = text ?: cancel!!.text) ?: actionOf(text ?: "")
        cancel!!.callbacks += callback
    }

    @JvmInline
    value class Response(val confirm: Boolean) : FormBuilder.Response

    override fun build(player: Player): ModalForm = Forms.modal()
        .title(title)
        .content(content)
        .trueButton(confirm?.text ?: "") {
            confirm?.callbacks?.forEach { it?.invoke(player) }
            val response = Response(true)
            response(player, response)
        }
        .falseButton(cancel?.text ?: "") {
            cancel?.callbacks?.forEach { it?.invoke(player) }
            val response = Response(false)
            response(player, response)
        }
        .onClose { response(player, null) }
}
