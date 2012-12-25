package jcdc.pluginfactory.examples

import jcdc.pluginfactory._
import EnrichmentClasses._
import org.bukkit.event.player.PlayerMoveEvent

class GetOffMyLawn extends ListenerPlugin with WorldEditCommands with Cubes {

  def movingOntoLawn(e:PlayerMoveEvent, lawn: Cube) =
    lawn.contains(e.getTo) && ! lawn.contains(e.getFrom)
  
  val listener = OnPlayerMove((p, e) =>
    for ((owner, lawn) <- cubes; if (p != owner && movingOntoLawn(e, lawn)))
      owner ! (s"${p.name} is on your lawn!")
  )

  val commands = List(
    pos1,
    pos2,
    Command(
      name = "GetOffMyLawn",
      desc = "Kick everyone off your lawn, and shocks them with lightning",
      body = noArgs(owner => run(owner)(lawn =>
        for(p <- lawn.players; if(p != owner)) {
          p.teleportTo(p.world.getHighestBlockAt(p.world(lawn.maxX + 5, 0, lawn.maxZ + 5).loc))
          p.shockWith(s"'Get off my lawn!!!', said ${owner.name}.")
        }
      ))
    )
  )
}
