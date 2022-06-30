package ua.cn.stu.junit

import java.util.concurrent.Executor

class TestExecutor(
    private val autoExec: Boolean = true
) : Executor {

    private val _commands = mutableListOf<Runnable>()
    val commands: List<Runnable> = _commands
    val invokeCount: Int get() = commands.size

    override fun execute(command: Runnable) {
        _commands.add(command)
        if (autoExec) {
            command.run()
        }
    }

}