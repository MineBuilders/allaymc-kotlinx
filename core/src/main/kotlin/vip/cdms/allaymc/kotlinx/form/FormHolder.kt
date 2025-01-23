package vip.cdms.allaymc.kotlinx.form

import vip.cdms.allaymc.kotlinx.Player

fun interface FormHolder<T : FormBuilder<*, *>> { infix fun evaluate(player: Player): T }
infix fun FormHolder<*>.sendTo(player: Player) = evaluate(player) sendTo player
infix fun Player.send(holder: FormHolder<*>) = holder sendTo this

fun ModalForm(block: ModalFormBuilder.(Player) -> Unit) = FormHolder { ModalFormBuilder { block(it) } }
fun SimpleForm(block: SimpleFormBuilder.(Player) -> Unit) = FormHolder { SimpleFormBuilder { block(it) } }

operator fun FormHolder<SimpleFormBuilder>.plus(other: FormHolder<SimpleFormBuilder>) =
    FormHolder { this.evaluate(it) + other.evaluate(it) }
operator fun FormHolder<SimpleFormBuilder>.plus(other: SimpleFormBuilder.(Player) -> Unit) =
    plus(SimpleForm(other))
