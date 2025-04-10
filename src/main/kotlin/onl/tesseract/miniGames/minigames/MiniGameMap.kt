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
import onl.tesseract.lib.inventory.InventoryInstanceManager
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
import org.bukkit.entity.Arrow
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.entity.TNTPrimed
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.concurrent.atomic.AtomicInteger

const val SPAWN = "spawn"
const val ARENA = "arena"
const val PLAYSPAWN = "playspawn"
private val STARTING_TITLE_COMPONENT = Component.text("Début de la partie dans", NamedTextColor.GOLD, TextDecoration.BOLD)
abstract class MiniGameMap(val name: String, var spawn: Location, private val miniGameName: String) : Listener {

    protected var arena : Cuboid = Cuboid(spawn,spawn)
    protected val schemFilePath = MINIGAMES_GAMES_FOLDER_NAME + "/" + this.javaClass.simpleName + "/" + name + ".schem"
    protected val schemFolderPath = MINIGAMES_GAMES_FOLDER_NAME+"/"+this.javaClass.simpleName
    val playSpawn = mutableSetOf<Location>()
    val players = mutableSetOf<Player>()
    val spectators = mutableSetOf<Player>()
    val playerScore = mutableMapOf<Player, Int>()
    val playerKills = mutableMapOf<Player, Int>()
    var arenaState: ArenaState = ArenaState.WAITING
    protected var pvpEnabled = false
    protected val dropOnDeath = false
    protected var freezeAtStart = true
    protected var lifeSystem = false
    protected var maxLife = 0
    val playersLifes = mutableMapOf<Player, Int>()

    open fun start() {
        if(players.size <= 1)return
        MiniGamesPlugin.instance.server.pluginManager.registerEvents(this, MiniGamesPlugin.instance)
        var i = 0
        for (p in players) {
            p.clearActivePotionEffects()
            p.teleport(playSpawn.elementAt(i++))
            setInventory(p)
            i %= playSpawn.size
            playersLifes[p] = maxLife
            p.maxHealth = 20.0
            p.health = p.maxHealth
            p.foodLevel = 20
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
        lp.teleport(spawn)
        broadcastTitle(Component.text("Victoire de ",NamedTextColor.GOLD)
                .append(lp.displayName().color(NamedTextColor.GOLD))
        ,Component.empty())
        sendRanking()
        FireworkHelper.fireWorkEffect(lp)
        unsetInventory(lp)
        players.remove(lp)
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
        players.clear()
        playerScore.clear()
        playersLifes.clear()
        playerKills.clear()
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
        when(arenaState){
            ArenaState.WAITING -> {}
            ArenaState.STARTING -> {
                if(freezeAtStart){
                    event.isCancelled = true
                }
            }
            ArenaState.INGAME -> {
                if (players.contains(event.player)) {
                    if (event.player.location !in arena) {
                        playerLeaveArena(event.player)
                    }
                }
            }
            ArenaState.ENDING -> {}
            ArenaState.DISABLED -> {}
        }
    }

    protected abstract fun playerLeaveArena(player: Player)

    open fun eliminatePlayer(player: Player) {
        playerScore[player] = players.size
        broadcast(getHeaderComponent()
                .append(player.displayName() )
                .append(" a été éliminé",NamedTextColor.RED))
        players.remove(player)
        player.isCollidable = true
        player.removePotionEffect(PotionEffectType.SATURATION)
        unsetInventory(player)
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

    open protected fun setInventory(player: Player){
        InventoryInstanceManager.selectConfig(player, miniGameName)
        player.inventory.clear()
    }

    open protected fun unsetInventory(player: Player){
        InventoryInstanceManager.selectConfig(player,"default")
    }

    protected fun sendRanking(){
        broadcast(Component.text("-----------------------",NamedTextColor.DARK_AQUA))
        lateinit var color : TextColor
        playerScore.toList().sortedBy { it.second }.forEach {
            color = ComponentHelper.getColorForRank(it.second)
            broadcast(Component.text("          ${it.second} - ",color)
                    .append(it.first.displayName().color(color))
                    .append(getRankingDetail(it.first)))
        }
        broadcast(Component.text("-----------------------",NamedTextColor.DARK_AQUA))
    }

    protected open fun getRankingDetail(player: Player): Component {
        return Component.empty()
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
    fun getRandomSpawn(): Location {
        return playSpawn.elementAt((0..<playSpawn.size).random())
    }
    abstract fun getHeaderComponent(): Component

    open fun playerKilled(player: Player, killer: Entity) {
        if (dropOnDeath) {
            for (itemStack in player.inventory) {
                player.world.dropItemNaturally(player.location, itemStack)
            }
        }
        if(killer is Player){
            playerKills[killer] = playerKills[killer]?.plus(1) ?: 1
        }
        if (lifeSystem) {
            playersLifes[player] = playersLifes[player]?.minus(1) ?: 0
            displayPlayerKilled(player, killer)
            if (playersLifes[player]!! <= 0) {
                eliminatePlayer(player)
            }
        }
        else{
            displayPlayerKilled(player, killer)
            eliminatePlayer(player)
        }
        player.health = 20.0

    }
    protected fun getDisplayPlayerComponent(player: Player) : Component{
        var component = player.displayName().color(NamedTextColor.GOLD);
        if(lifeSystem){
            if((playersLifes[player] ?: 0) > 0){
                component = component.append(" (",NamedTextColor.YELLOW)
                for (i in 1..(playersLifes[player] ?: 1)) {
                    component = component.append("♥", NamedTextColor.RED)
                }
                component = component.append(") ",NamedTextColor.YELLOW)
            }

        }
        return component
    }
    protected open fun displayPlayerKilled(player: Player, killer: Entity) {
        var component : Component = Component.empty()
        component = if(killer is Player){
            component.append(getDisplayPlayerComponent(killer))
        }else{
            component.append(killer.name())
        }

        component = component.append(" a vaincu ", NamedTextColor.YELLOW)

        component = component.append(getDisplayPlayerComponent(player))

        broadcast(component)
    }

    @EventHandler
    fun onDamage(event : EntityDamageEvent) {
        if (event.entity.type != EntityType.PLAYER)
            return
        val player = event.entity as Player
        if(player !in players){
            return
        }
        if(arenaState != ArenaState.INGAME){
            event.isCancelled = true
            return
        }
        if(!pvpEnabled)
            event.isCancelled = true
    }


    @EventHandler
    fun onPlayerDeath(event: EntityDamageByEntityEvent) {
        if(event.entity !is Player)return
        val player = event.entity as Player
        if(player !in players){return}
        if(player.health - event.damage <= 0){
            var killer = event.damager;
            if(event.damager is Arrow){
                val shooter = (event.damager as Arrow).shooter
                if(shooter is Player){
                    killer = shooter
                }
            }else if (event.damager is TNTPrimed){
                val source = (event.damager as TNTPrimed).source
                if(source is Player){
                    killer = source
                }
            }

            playerKilled(player, killer)
            event.isCancelled = true
        }
    }
}