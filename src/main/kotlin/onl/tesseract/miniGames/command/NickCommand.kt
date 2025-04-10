package onl.tesseract.miniGames.command

import net.kyori.adventure.text.Component
import onl.tesseract.commandBuilder.CommandContext
import onl.tesseract.commandBuilder.annotation.Argument
import onl.tesseract.commandBuilder.annotation.Command
import onl.tesseract.lib.command.argument.StringArg
import onl.tesseract.miniGames.command.arguments.OnlinePlayerArg
import org.bukkit.entity.Player

@Command(name = "nick")
class NickCommand : CommandContext(){

    @Command(name = "set")
    fun setCommand(@Argument(value = "player", clazz = OnlinePlayerArg::class) player: Player,@Argument(value = "name", clazz = StringArg::class) name: String){
        player.displayName(Component.text(name))
    }
}