package vip.cdms.allaymc.kotlinx.form

import org.allaymc.api.entity.interfaces.EntityPlayer
import org.allaymc.api.form.Forms
import org.allaymc.api.form.element.Button
import org.allaymc.api.form.element.ImageData
import org.allaymc.api.form.element.ImageData.PATH_TYPE
import org.allaymc.api.form.element.ImageData.URL_TYPE
import org.allaymc.api.form.type.SimpleForm
import vip.cdms.allaymc.kotlinx.Receiver

class SimpleFormBuilder : FormBuilder<SimpleForm, SimpleFormBuilder.Response>() {
    var content = ""

    sealed interface Image {
        val data: String
        data class Path(override val data: String) : Image
        data class Url(override val data: String) : Image
    }
    data class Button(val text: String, val image: Image? = null)
    fun imagePathOf(path: String) = Image.Path(path)
    fun imageUrlOf(url: String) = Image.Url(url)
    fun Image.convert() = ImageData(if (this is Image.Path) PATH_TYPE else URL_TYPE, data)
    fun Button.convert() = Button(text, image?.convert())
    val buttons = mutableListOf<Button>()
    val callbacks = linkedMapOf<Button, MutableList<Receiver<EntityPlayer>?>>()

    fun buttonOf(text: String, image: Image? = null, callback: Receiver<EntityPlayer>? = null) =
        Button(text, image).apply { callbacks.getOrPut(this) { mutableListOf() } += callback }
    fun button(text: String, image: Image? = null, callback: Receiver<EntityPlayer>? = null) =
        buttonOf(text, image, callback).apply { buttons += this }

    operator fun plus(block: Receiver<SimpleFormBuilder>) = plus(SimpleFormBuilder(block))
    operator fun plus(builder: SimpleFormBuilder) = SimpleFormBuilder outputBuilder@ {
        this@outputBuilder.title = builder.title.ifBlank { this@SimpleFormBuilder.title }
        this@outputBuilder.content = builder.content.ifBlank { this@SimpleFormBuilder.content }
        this@outputBuilder.buttons += this@SimpleFormBuilder.buttons + builder.buttons
        this@outputBuilder.callbacks += this@SimpleFormBuilder.callbacks + builder.callbacks
    }

    @JvmInline
    value class Response(val index: Int) : FormBuilder.Response

    override fun build(player: EntityPlayer): SimpleForm = Forms.simple()
        .title(title)
        .content(content)
        .apply {
            this@SimpleFormBuilder.buttons.forEachIndexed { index, it ->
                val button = it.convert()
                button.setOnClick { _ ->
                    callbacks[it]?.forEach { it?.invoke(player) }
                    val response = Response(index)
                    response(player, response)
                }
                button(button)
            }
        }
        .onClose { response(player, null) }
}
