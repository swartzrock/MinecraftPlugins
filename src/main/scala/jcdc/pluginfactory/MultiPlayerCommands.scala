package jcdc.pluginfactory

import org.bukkit.command.Command
import scala.collection.JavaConversions._
import org.bukkit.entity._
import org.bukkit.{GameMode, ChatColor}
import ChatColor._
import ScalaPlugin._

class MultiPlayerCommands extends ManyCommandsPlugin {

  val commands = Map(
    "goto" -> goto,
    "gm" -> gameModeChanger,
    "kill" -> opOnly(killHandler),
    "set-time" -> changeTime,
    "day" -> dayMaker,
    "night" -> nightMaker,
    "spawn" -> spawner,
    "entities" -> showEntities,
    "feed" -> opOnly(feedHandler),
    "starve" -> opOnly(starveHandler))

  val gameModeChanger = oneArg((p:Player, c:Command, args:Array[String]) =>
    if(List("c", "s").contains(args(0))) p.sendUsage(c)
    else p.setGameMode(if(args(0) == "c") GameMode.CREATIVE else GameMode.SURVIVAL))
  val killHandler = oneArg((killer:Player, c:Command, args:Array[String]) => {
    val world = killer.getWorld
    val entities = world.getEntities
    def usage(){ killer.sendUsage(c) }
    def removeAll(es:Seq[Entity]) { es.foreach(_.remove()) }
    args(0).toLowerCase match {
      case "player" => if(args.length == 2) killer.kill(args(1)) else usage()
      case "items" => removeAll(entities.collect{ case i: Item => i })
      case "chickens" => removeAll(entities.collect{ case i: Chicken => i })
      case _ => usage()
    }
  })
  val showEntities = (p:Player, c:Command, args:Array[String]) =>
    p.getWorld.getEntities.foreach(e => p.sendMessage(e.toString))
  val goto = oneArg(p2p((sender:Player, receiver:Player, c:Command, args:Array[String]) =>
    sender.teleport(receiver)))
  val feedHandler = oneArg(p2p((feeder:Player, receiver:Player, c:Command, args:Array[String]) => {
    receiver.messageAfter(GREEN + "you have been fed by " + feeder.getName){ receiver.setFoodLevel(20) }
    feeder.sendMessage(GREEN + "you have fed" + feeder.getName)
  }))
  val starveHandler = oneArg(p2p((feeder:Player, receiver:Player, c:Command, args:Array[String]) => {
    receiver.messageAfter(GREEN + "you have been starved by " + feeder.getName){ receiver.setFoodLevel(0) }
    feeder.sendMessage(GREEN + "you have starved " + feeder.getName)
  }))
  val changeTime = oneArg((p:Player, c:Command, args:Array[String]) => p.getWorld.setTime(args(0).toInt))
  val dayMaker = (p:Player, c:Command, args:Array[String]) => p.getWorld.setTime(1)
  val nightMaker = (p:Player, c:Command, args:Array[String]) => p.getWorld.setTime(15000)
  val spawner = oneArg((p: Player, c: Command, args: Array[String]) =>
    CreatureType.values.find(_.toString == args(0).toUpperCase) match {
      case Some(creature) =>
        // TODO: its probably a really good idea to put some limit on N here.
        for (i <- 1 to (if (args.length == 2) args(1).toInt else 1)) {
          p.getWorld.spawnCreature(p.getLocation, creature)
        }
      case _ => p.sendError("no such creature: " + args(0))
    })
}