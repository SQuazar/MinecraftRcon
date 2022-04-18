package net.flawe.rcon.model;

public record Packet(int length,
                     int requestId,
                     int packetType,
                     byte[] payload) {
}
