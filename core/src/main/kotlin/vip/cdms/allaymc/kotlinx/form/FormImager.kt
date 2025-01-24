package vip.cdms.allaymc.kotlinx.form

import org.allaymc.api.form.element.ImageData

// for better DSL :)
interface FormImager {
    sealed interface Image {
        val data: String
        data class Path(override val data: String) : Image
        data class Url(override val data: String) : Image
    }

    fun imagePathOf(path: String) = Image.Path(path)
    fun imageUrlOf(url: String) = Image.Url(url)

    fun Image.convert() = ImageData(
        if (this is Image.Path)
            ImageData.ImageType.PATH
        else
            ImageData.ImageType.URL,
        data
    )
}

typealias FormImage = FormImager.Image
