package net.flawe.rcon;

import net.flawe.rcon.exception.RconAlreadyConnectedException;
import net.flawe.rcon.exception.RconConnectionException;
import net.flawe.rcon.exception.RconLoginException;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Bad arguments! Expected arguments: login, port, password");
            return;
        }
        String host = args[0];
        int port;
        try {
            port = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.out.println("Bad port value in arguments!");
            return;
        }
        String password = args[2];
        var rcon = new Rcon(host, port, password);
        try {
            var connectionResponse = rcon.connect();
            switch (connectionResponse) {
                case BAD_LOGIN -> throw new RconLoginException("Bad login");
                case SUCCESS -> System.out.println(">> RCON successful connected!");
            }
            Scanner scanner = new Scanner(System.in);
            System.out.print(">> ");
            while (scanner.hasNextLine()) {
                var command = scanner.nextLine();
                if (command.equalsIgnoreCase(".clear")) {
                    System.out.print("\033[H\033[2J");
                    System.out.flush();
                    continue;
                }
                if (command.equalsIgnoreCase(".stop")) {
                    System.out.println(">> Disconnecting...");
                    rcon.disconnect();
                    return;
                }
                var response = rcon.sendCommand(command);
                if (!response.isEmpty()) {
                    System.out.printf(">> %s", response);
                    if (response.charAt(response.length() - 1) != '\n') System.out.println();
                }
                System.out.print(">> ");
            }
        } catch (RconConnectionException | RconLoginException | RconAlreadyConnectedException e) {
            System.out.printf("[Error] %s%n", e.getMessage());
        }
    }

}
