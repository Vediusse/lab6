package viancis.lab6.server;

import viancis.lab6.common.commands.CommandType;
import viancis.lab6.server.commands.InterfaceCommand;
import viancis.lab6.common.communication.Response;
import viancis.lab6.common.messages.Category;
import viancis.lab6.common.messages.Message;
import viancis.lab6.common.messages.Sender;
import viancis.lab6.server.collection.Collection;
import viancis.lab6.common.communication.Request;
import viancis.lab6.server.commands.builder.ComandBuilder;
import viancis.lab6.server.logger.CustomLogger;

import java.io.*;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;

import java.nio.ByteBuffer;
import java.util.HashMap;


public class ServerHandler {
    private static final int BUFFER_SIZE = 65507;
    private final Collection collection;
    public DatagramSocket socket;
    public final Sender sender;
    private HashMap<String, InterfaceCommand> commandMap;
    private static final String ENV_NAME = "/logs/log.logs";
    private static final String PATH = "/file/default.xml";

    private final CustomLogger logger = new CustomLogger();

    private HashMap<String,CommandType> commandTypeMap = null;

    public ServerHandler(Sender sender, Collection collection) {
        this.socket = setSocket();
        this.sender = sender;
        this.createCommands();
        this.collection = collection;
    }

    public DatagramSocket setSocket() {
        try {
            return new DatagramSocket(9876);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void handleRequests() {
        while (true) {
            try {
                byte[] buffer = new byte[BUFFER_SIZE];
                DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
                socket.receive(receivePacket);
                ByteArrayInputStream byteStream = new ByteArrayInputStream(receivePacket.getData());
                ObjectInputStream objectStream = new ObjectInputStream(byteStream);

                Request receivedRequest = (Request) objectStream.readObject();

                if (!receivedRequest.command().equalsIgnoreCase("execute_script") && !receivedRequest.command().trim().isEmpty()) {
                    if (receivedRequest.command().equalsIgnoreCase("commands")) {
                        Response response = new Response("Available commands", true, this.commandTypeMap);
                        sendResponse(response, receivePacket.getAddress(), receivePacket.getPort());
                    } else {
                        InterfaceCommand command = commandMap.get(receivedRequest.command());
                        if (command != null) {
                            Response response = command.execute(receivedRequest, this.collection);
                            sendResponse(response, receivePacket.getAddress(), receivePacket.getPort());
                        } else {
                            sender.printMessage(new Message(Category.WARNING, "Command not found: " + receivedRequest.command()));
                            this.logger.logMessageToFile(Category.WARNING, "Command not found: " + receivedRequest.command());
                        }
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                this.logger.logMessageToFile(Category.WARNING, "IO error");
            } catch (Exception e) {
                e.printStackTrace();
                this.handleRequests();
            }
        }
    }

    private void sendResponse(Response response, InetAddress address, int port) {
        try {
            ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteOutputStream);
            objectOutputStream.writeObject(response);
            objectOutputStream.flush();

            byte[] responseData = byteOutputStream.toByteArray();
            int dataSize = responseData.length;

            ByteBuffer sizeBuffer = ByteBuffer.allocate(Integer.BYTES);
            sizeBuffer.putInt(dataSize);
            byte[] sizeData = sizeBuffer.array();
            DatagramPacket sizePacket = new DatagramPacket(sizeData, sizeData.length, address, port);
            socket.send(sizePacket);

            DatagramPacket responsePacket = new DatagramPacket(responseData, dataSize, address, port);
            socket.send(responsePacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void close() {
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
    }

    public void createCommands() {
        ComandBuilder builder = new ComandBuilder();
        this.commandMap = builder.createCommands(sender);
        this.commandTypeMap = builder.getCommandType(sender);
    }

}