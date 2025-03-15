package onl.tesseract.miniGames.minigames


import com.fastasyncworldedit.core.FaweAPI
import com.sk89q.worldedit.WorldEdit
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard
import com.sk89q.worldedit.extent.clipboard.Clipboard
import com.sk89q.worldedit.extent.clipboard.io.BuiltInClipboardFormat
import com.sk89q.worldedit.function.operation.ForwardExtentCopy
import com.sk89q.worldedit.function.operation.Operation
import com.sk89q.worldedit.function.operation.Operations
import com.sk89q.worldedit.math.BlockVector3
import com.sk89q.worldedit.regions.CuboidRegion
import com.sk89q.worldedit.session.ClipboardHolder
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import onl.tesseract.lib.util.append
import onl.tesseract.miniGames.MiniGamesPlugin
import onl.tesseract.miniGames.utils.MINIGAMES_GAMES_FOLDER_NAME
import onl.tesseract.miniGames.utils.cuboid.Cuboid
import onl.tesseract.miniGames.utils.enums.ArenaState
import onl.tesseract.miniGames.utils.helpers.ComponentHelper
import onl.tesseract.miniGames.utils.helpers.FireworkHelper
import onl.tesseract.miniGames.utils.helpers.TitleHelper
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.scheduler.BukkitRunnable
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.concurrent.atomic.AtomicInteger

const val SPAWN = "spawn"
const val ARENA = "arena"
const val PLAYSPAWN = "playspawn"
private val STARTING_TITLE_COMPONENT = Component.text("Début de la partie dans", NamedTextColor.GOLD, TextDecoration.BOLD)
abstract class MiniGameMap(val name: String, var spawn: Location) : Listener {

    protected var arena : Cuboid = Cuboid(spawn,spawn)
    protected val schemFilePath = MINIGAMES_GAMES_FOLDER_NAME + "/" + this.javaClass.simpleName + "/" + name + ".schem"
    protected val schemFolderPath = MINIGAMES_GAMES_FOLDER_NAME+"/"+this.javaClass.simpleName
    val playSpawn = mutableSetOf<Location>()
    val players = mutableSetOf<Player>()
    val spectators = mutableSetOf<Player>()
    val playerScore = mutableMapOf<Player, Int>()
    var arenaState: ArenaState = ArenaState.WAITING

    open fun start() {
        MiniGamesPlugin.instance.server.pluginManager.registerEvents(this, MiniGamesPlugin.instance)
        var i = 0
        for (p in players) {
            p.teleport(playSpawn.elementAt(i++))
            i %= playSpawn.size
        }
        val countDown = AtomicInteger(5)
        object : BukkitRunnable() {
            override fun run() {
                val subtitle = Component.text("$countDown", NamedTextColor.GREEN, TextDecoration.BOLD)
                broadcastTitle(STARTING_TITLE_COMPONENT,subtitle)

                if (countDown.decrementAndGet() < 0) {
                    this.cancel()
                    for (player in players) {
                        TitleHelper.clearTitle(player)
                    }
                    for (player in spectators) {
                        TitleHelper.clearTitle(player)
                    }
                    arenaState = ArenaState.INGAME
                }
            }
        }.runTaskTimer(MiniGamesPlugin.instance, 20,20)
        arenaState = ArenaState.STARTING
    }


    open fun stop(){
        this.arenaState = ArenaState.ENDING
        val lp = players.elementAt(0)
        playerScore[lp] = 1
        players.remove(lp)
        lp.teleport(spawn)
        broadcastTitle(Component.text("Victoire de ")
                .append(lp.displayName())
        ,Component.empty())
        sendRanking()
        FireworkHelper.fireWorkEffect(lp)
        object : BukkitRunnable() {
            override fun run() {
                this@MiniGameMap.resetArena()
            }
        }.runTaskLaterAsynchronously(MiniGamesPlugin.instance,10)

    }

    open fun save(createSection: ConfigurationSection) {
        createSection[SPAWN] = spawn
        createSection[ARENA] = arena.serialize()
        createSection[PLAYSPAWN] = playSpawn.toList()
    }

    fun setArena(x1: Double, y1: Double, z1: Double, x2: Double, y2: Double, z2: Double) {
        arena.resetCorner(x1,y1,z1,x2,y2,z2)

        val region = CuboidRegion(BlockVector3.at(arena.high.x,arena.high.y,arena.high.z), BlockVector3.at(arena.low.x,arena.low.y,arena.low.z))
        val world = FaweAPI.getWorld(arena.high.world.name)
        val clipboard = BlockArrayClipboard(region)

        val forwardExtentCopy = ForwardExtentCopy(
            world, region, clipboard, BlockVector3.at(arena.low.x,arena.low.y,arena.low.z)
        )

        // configure here
        Operations.complete(forwardExtentCopy)


        val file = File(this.schemFilePath)
        val folder = File(this.schemFolderPath)
        if(!folder.exists()){
            folder.mkdirs()
        }
        if (!file.exists()) {
            file.createNewFile()
        }
        BuiltInClipboardFormat.FAST_V3.getWriter(FileOutputStream(file))
                .use { writer ->
                    writer.write(clipboard)
                }

    }

    fun resetArena() {
        val clipboard: Clipboard
        val file = File(this.schemFilePath)
        val format = BuiltInClipboardFormat.FAST_V3
        format.getReader(FileInputStream(file))
                .use { reader ->
                    clipboard = reader.read()
                }
        val world = FaweAPI.getWorld(arena.high.world.name)
        WorldEdit.getInstance()
                .newEditSession(world)
                .use { editSession ->
                    val operation: Operation = ClipboardHolder(clipboard)
                            .createPaste(editSession)
                            .to(BlockVector3.at(arena.low.x,arena.low.y,arena.low.z)) // configure here
                            .build()
                    Operations.complete(operation)
                }

        this.arenaState = ArenaState.WAITING
        this.players.clear()
    }


    @EventHandler
    fun onPlayerMove(event: PlayerMoveEvent) {
        if (arenaState == ArenaState.INGAME) {
            if (players.contains(event.player)) {
                if (event.player.location !in arena) {
                    playerLeaveArena(event.player)
                }
            }
        }
    }

    protected abstract fun playerLeaveArena(player: Player)

    fun eliminatePlayer(player: Player) {
        playerScore[player] = players.size
        players.remove(player)
        broadcast(getHeaderComponent()
                .append(player.displayName() )
                .append(" a été éliminé",NamedTextColor.RED))
        player.teleport(this.spawn)
        joinSpectator(player)
        if(checkEnd()) {
            stop()
        }
    }

    open protected fun checkEnd() : Boolean{
        return players.size <= 1
    }

    fun joinSpectator(player: Player) {
        spectators.add(player)
        player.gameMode = GameMode.SPECTATOR
        player.teleport(spawn)
    }


    fun join(player: Player) {
        if (arenaState == ArenaState.WAITING) {
            if (player in players) {
                player.sendMessage(
                    getHeaderComponent()
                            .append(Component.text("Vous êtes déjà en jeu", NamedTextColor.YELLOW)))
                return
            }
            if (player in spectators) {
                spectators.remove(player)
            }
            players.add(player)
            player.teleport(spawn)
            player.gameMode = GameMode.ADVENTURE
            broadcast(
                getHeaderComponent()
                        .append(player.displayName())
                        .append(Component.text(" a rejoind la partie ", NamedTextColor.YELLOW)))
            return
        }

        player.sendMessage(
            getHeaderComponent()
                    .append(Component.text("La partie n'est pas en attente", NamedTextColor.YELLOW)))


    }

    protected fun sendRanking(){
        broadcast(Component.text("-----------------------",NamedTextColor.DARK_AQUA))
        lateinit var color : TextColor
        playerScore.toList().sortedBy { it.second }.forEach {
            color = ComponentHelper.getColorForRank(it.second)
            broadcast(Component.text("          ${it.second} - ",color)
                    .append(it.first.displayName().color(color)))
        }
        broadcast(Component.text("-----------------------",NamedTextColor.DARK_AQUA))
    }


    protected fun broadcast(component: Component) {
        for (p in players) {
            p.sendMessage(component)
        }
        for (p in spectators) {
            p.sendMessage(component)
        }
    }
    protected fun broadcastTitle(title: Component, subtitle: TextComponent) {
        for (p in players) {
            TitleHelper.sendTitle(p,title,subtitle)
        }
        for (p in spectators) {
            TitleHelper.sendTitle(p,title,subtitle)
        }
    }

    abstract fun getHeaderComponent(): Component

}