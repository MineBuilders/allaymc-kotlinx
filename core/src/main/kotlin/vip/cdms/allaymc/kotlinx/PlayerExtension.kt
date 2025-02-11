package vip.cdms.allaymc.kotlinx

import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket

infix fun Player.send(packet: BedrockPacket) = sendPacket(packet)
