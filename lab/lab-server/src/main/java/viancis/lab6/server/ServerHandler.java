package viancis.lab6.server;

import viancis.lab6.common.commands.InterfaceCommand;
import viancis.lab6.common.communication.ClientInput; // TODO useless
import viancis.lab6.common.communication.Response;
import viancis.lab6.common.messages.Category;
import viancis.lab6.common.messages.Message;
import viancis.lab6.common.messages.Sender;
import viancis.lab6.server.collection.Collection;
import viancis.lab6.common.communication.Request;

import java.io.*;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Arrays; // TODO useless
import java.util.HashMap;
import java.util.List; // TODO useless
import java.util.Map; // TODO useless

class ServerHandler {
    private static final int BUFFER_SIZE = 65507;
    private final Collection collection;
    private DatagramSocket socket;
    private final Sender sender;
    private final HashMap<String, InterfaceCommand> commandMap;

    public ServerHandler(Sender sender, HashMap<String, InterfaceCommand> commandMap, Collection collection) {
        this.socket = setSocket();
        this.sender = sender;
        this.commandMap = commandMap;
        this.collection = collection;
    }
    public DatagramSocket setSocket(){
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
                sender.printMessage(new Message(Category.WARNING, commandMap.toString()));
                if (!receivedRequest.command().equalsIgnoreCase("execute_script") &&
                        !receivedRequest.command().trim().isEmpty()) {

                    InterfaceCommand command = commandMap.get(receivedRequest.command());
                    if (command != null) {
                        receivedRequest.setPriorityQueue(this.collection.getMusicBands());
                        // TODO : Request.setPriorityQueue(this.collection.getMusicBands()); // btw static method
                        Response response = command.execute(receivedRequest);
                        DatagramPacket responsePacket = getDatagramPacket(response, receivePacket);
                        socket.send(responsePacket);
                    } else {
                        sender.printMessage(new Message(Category.ERROR, "Command not found: " + receivedRequest.command()));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static DatagramPacket getDatagramPacket(Response response, DatagramPacket receivePacket) throws IOException {
        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteOutputStream);
        objectOutputStream.writeObject(response);
        byte[] responseData = byteOutputStream.toByteArray();

        DatagramPacket responsePacket = new DatagramPacket(responseData, responseData.length, receivePacket.getAddress(), receivePacket.getPort());
        return responsePacket;
    }


    private void sendInfoMessage(InetAddress address, int port) { // TODO never used localy
        try {
            String infoMessage = "INFO: Received and processed the message successfully";
            byte[] infoData = infoMessage.getBytes();
            DatagramPacket infoPacket = new DatagramPacket(infoData, infoData.length, address, port);
            socket.send(infoPacket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}