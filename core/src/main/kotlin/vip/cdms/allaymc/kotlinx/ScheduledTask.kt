package vip.cdms.allaymc.kotlinx

import org.allaymc.api.server.Server
import java.util.concurrent.atomic.AtomicBoolean
import java.util.function.Consumer
import kotlin.math.roundToInt
import kotlin.time.Duration

sealed interface ScheduledTask {
    val id: Int
    fun clear()

    @JvmInline
    value class Timeout(override val id: Int) : ScheduledTask {
        override fun clear() = clearTimeout(this)
    }

    @JvmInline
    value class Interval(override val id: Int) : ScheduledTask {
        override fun clear() = clearInterval(this)
    }

    // java friendly :)
    companion object {
        @JvmStatic
        val TimeoutTasks = mutableMapOf<Timeout, AtomicBoolean>()

        @JvmStatic
        val IntervalTasks = mutableMapOf<Interval, AtomicBoolean>()

        @Volatile
        internal var TimeoutIds = 0

        @Volatile
        internal var IntervalIds = 0

        fun setTimeout(
            delay: Int = 20,
            async: Boolean = false,
            task: () -> Unit
        ): Timeout {
            val id = Timeout(TimeoutIds++)
            val canceled = AtomicBoolean(false)
            TimeoutTasks[id] = canceled
            Server.getInstance().scheduler.scheduleDelayed(
                { !canceled.get() },
                { task(); true },
                delay,
                async
            )
            return id
        }

        fun setInterval(
            delay: Int = 0,
            period: Int = 20,
            async: Boolean = false,
            task: (id: Interval) -> Unit
        ): Interval {
            val id = Interval(IntervalIds++)
            val canceled = AtomicBoolean(false)
            IntervalTasks[id] = canceled
            Server.getInstance().scheduler.scheduleDelayedRepeating(
                { !canceled.get() },
                { task(id); true },
                delay,
                period,
                async
            )
            return id
        }

        @JvmStatic
        fun setTimeout(delay: Int, async: Boolean, task: Runnable) =
            setTimeout(delay, async) { task.run() }

        @JvmStatic
        fun setTimeout(delay: Int, task: Runnable) =
            setTimeout(delay, false, task)

        @JvmStatic
        fun setInterval(delay: Int, period: Int, async: Boolean, task: Consumer<Interval>) =
            setInterval(delay, period, async) { task.accept(it) }

        @JvmStatic
        fun setInterval(delay: Int, period: Int, task: Consumer<Interval>) =
            setInterval(delay, period, false, task)

        @JvmStatic
        fun setInterval(period: Int, async: Boolean, task: Consumer<Interval>) =
            setInterval(0, period, async, task)

        @JvmStatic
        fun setInterval(period: Int, task: Consumer<Interval>) =
            setInterval(period, false, task)

        @JvmStatic
        fun clearTimeout(id: Timeout) {
            TimeoutTasks[id]?.set(true)
        }

        @JvmStatic
        fun clearInterval(id: Interval) {
            IntervalTasks[id]?.set(true)
        }
    }
}

private val Duration.inTicks get() = (inWholeMilliseconds / 1000.0 * 20).roundToInt()
