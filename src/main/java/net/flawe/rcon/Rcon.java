package net.flawe.rcon;

import net.flawe.rcon.exception.RconAlreadyConnectedException;
import net.flawe.rcon.exception.RconConnectionException;
import net.flawe.rcon.model.ConnectionResponse;
import net.flawe.rcon.model.Packet;
import net.flawe.rcon.model.PacketType;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ThreadLocalRandom;

public class Rcon {

    private final String host;
    private final int port;
    private final String password;
    private final Charset charset = StandardCharsets.UTF_8;
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private int requestId;

    public Rcon(String host, int port, String password) {
        this.host = host;
        this.port = port;
        this.password = password;
    }

    public synchronized ConnectionResponse connect() {
        if (socket != null)
            throw new RconAlreadyConnectedException("Create new instance of this class!");
        try {
            socket = new Socket(host, port);
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            requestId = ThreadLocalRandom.current().nextInt();
            var packet = sendPacket(PacketType.LOGIN, password.getBytes(charset));
            if (packet.requestId() != requestId)
                return ConnectionResponse.BAD_LOGIN;
            return ConnectionResponse.SUCCESS;
        } catch (IOException e) {
            throw new RconConnectionException(String.format("Cannot connect to the server!%n %s %d", host, port), e);
        }
    }

    public synchronized void disconnect() {
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized String sendCommand(String command) {
        return new String(sendPacket(PacketType.COMMAND, command.getBytes(charset)).payload(), charset);
    }

    private Packet sendPacket(int packetType, byte[] payload) {
        write(packetType, payload);
        return read();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private Packet read() {
        byte[] arr = new byte[12];
        try {
            inputStream.read(arr);
            var buffer = ByteBuffer.wrap(arr);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            int length = buffer.getInt();
            int requestId = buffer.getInt();
            int packetType = buffer.getInt();
            byte[] payload = new byte[length - 4 - 4 - 2];
            inputStream.read(payload);
            inputStream.read(new byte[2]);
            return new Packet(length, requestId, packetType, payload);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void write(int packetType, byte[] payload) {
        int length = 4 + 4 + payload.length + 2;
        int packetLength = 4 + length;
        var byteBuffer = ByteBuffer.allocate(packetLength);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        byteBuffer.putInt(length);
        byteBuffer.putInt(requestId);
        byteBuffer.putInt(packetType);
        byteBuffer.put(payload);
        byteBuffer.put((byte) 0);
        byteBuffer.put((byte) 0);
        try {
            outputStream.write(byteBuffer.array());
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}