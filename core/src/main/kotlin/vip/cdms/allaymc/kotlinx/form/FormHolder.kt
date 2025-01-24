package vip.cdms.allaymc.kotlinx.form

import vip.cdms.allaymc.kotlinx.Player

fun interface FormHolder<T : FormBuilder<*, *>> { infix fun evaluate(player: Player): T }
infix fun FormHolder<*>.sendTo(player: Player) = evaluate(player) sendTo player
infix fun Player.send(holder: FormHolder<*>) = holder sendTo this

fun ModalForm(block: ModalFormBuilder.(Player) -> Unit) = FormHolder { ModalFormBuilder { block(it) } }
fun SimpleForm(block: SimpleFormBuilder.(Player) -> Unit) = FormHolder { SimpleFormBuilder { block(it) } }
fun CustomForm(block: CustomFormBuilder.(Player) -> Unit) = FormHolder { CustomFormBuilder { block(it) } }

@JvmName("plusSimpleFormHolder")
operator fun FormHolder<SimpleFormBuilder>.plus(other: FormHolder<SimpleFormBuilder>) =
    FormHolder { this.evaluate(it) + other.evaluate(it) }
@JvmName("plusSimpleFormHolderBlock")
operator fun FormHolder<SimpleFormBuilder>.plus(other: SimpleFormBuilder.(Player) -> Unit) =
    plus(SimpleForm(other))

@JvmName("plusCustomFormBuilder")
operator fun FormHolder<CustomFormBuilder>.plus(other: FormHolder<CustomFormBuilder>) =
    FormHolder { this.evaluate(it) + other.evaluate(it) }
@JvmName("plusCustomFormHolderBlock")
operator fun FormHolder<CustomFormBuilder>.plus(other: CustomFormBuilder.(Player) -> Unit) =
    plus(CustomForm(other))
