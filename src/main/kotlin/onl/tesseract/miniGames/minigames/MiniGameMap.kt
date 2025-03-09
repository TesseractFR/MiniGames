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
import onl.tesseract.miniGames.utils.MINIGAMES_GAMES_FOLDER_NAME
import onl.tesseract.miniGames.utils.cuboid.Cuboid
import org.bukkit.Location
import org.bukkit.configuration.ConfigurationSection
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream


abstract class MiniGameMap(val name: String,val spawn : Location) {

    protected var arena : Cuboid = Cuboid(spawn,spawn)
    protected val schemFilePath = MINIGAMES_GAMES_FOLDER_NAME + "/" + this.javaClass.simpleName + "/" + name + ".schem"
    protected val schemFolderPath = MINIGAMES_GAMES_FOLDER_NAME+"/"+this.javaClass.simpleName
    val playSpawn = mutableListOf<Location>()

    abstract fun start()
    abstract fun stop()
    abstract fun save(createSection: ConfigurationSection)
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
    }

}