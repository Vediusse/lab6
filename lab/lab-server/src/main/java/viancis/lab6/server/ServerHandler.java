package viancis.lab6.server;

import viancis.lab6.common.commands.CommandType;
import viancis.lab6.common.models.User;
import viancis.lab6.server.commands.InterfaceCommand;
import viancis.lab6.common.communication.Response;
import viancis.lab6.common.messages.Category;
import viancis.lab6.common.messages.Message;
import viancis.lab6.common.messages.Sender;
import viancis.lab6.server.collection.Collection;
import viancis.lab6.common.communication.Request;
import viancis.lab6.server.commands.builder.ComandBuilder;
import viancis.lab6.server.db.ConnectionBataDase;
import viancis.lab6.server.db.JwtGenerator;
import viancis.lab6.server.db.ManagerDb;
import viancis.lab6.server.logger.CustomLogger;

import java.io.*;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ServerHandler {
    private static final int BUFFER_SIZE = 65507;
    private Collection collection;

    private final Map<String, Integer> addressRequestCount = new ConcurrentHashMap<>();
    private final Map<String, Long> blockedAddresses = new ConcurrentHashMap<>();

    public DatagramSocket socket;
    public final Sender sender;
    private HashMap<String, InterfaceCommand> commandMap;

    private static final String PATH = "/file/default.xml";

    private static final int MAX_REQUESTS_PER_MINUTE = 20;
    private static final long BLOCK_DURATION_MINUTES = 2;


    private HashMap<String, CommandType> commandTypeMap = null;

    private final JwtGenerator jwtGenerator = new JwtGenerator();
    private ManagerDb db;

    public ServerHandler(Sender sender, Collection collection) {
        this.socket = setSocket();
        this.sender = sender;
        this.createCommands();
        this.collection = collection;
    }

    public DatagramSocket setSocket() {
        try {
            return new DatagramSocket(1095);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }


    private final ExecutorService requestReaderPool = Executors.newFixedThreadPool(10);

    public void startRequestReading() {
        while (true) {
            try {
                byte[] buffer = new byte[BUFFER_SIZE];
                DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
                socket.receive(receivePacket);
                ByteArrayInputStream byteStream = new ByteArrayInputStream(receivePacket.getData());
                ObjectInputStream objectStream = new ObjectInputStream(byteStream);
                this.db = new ManagerDb(new ConnectionBataDase());


                Request receivedRequest = (Request) objectStream.readObject();

                requestReaderPool.execute(() -> {
                    try {
                        processRequest(receivedRequest, receivePacket.getAddress(), receivePacket.getPort());
                    } catch (InterruptedException | IOException e) {
                        throw new RuntimeException(e);
                    }
                });

            } catch (IOException | ClassNotFoundException e) {

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }


    private final ExecutorService requestProcessorPool = Executors.newFixedThreadPool(10);

    public void processRequest(Request request, InetAddress address, int port) throws InterruptedException, IOException {
        String ipAddress = address.getHostAddress();

        if (isAddressBlocked(ipAddress)) {
            sendBlockedResponse(address, port);
            return;
        }

        int requestCount = addressRequestCount.getOrDefault(ipAddress, 0) + 1;

        addressRequestCount.put(ipAddress, requestCount);

        if (requestCount > MAX_REQUESTS_PER_MINUTE) {
            blockAddress(ipAddress);
            sendBlockedResponse(address, port);
            return;
        }

        if (!isValidCommand(request)) {
            return;
        }

        if (request.command().equalsIgnoreCase("commands")) {
            Response response = new Response("Available commands", true, this.commandTypeMap);
            sendResponse(response, address, port);
            return;
        }

        InterfaceCommand command = commandMap.get(request.command());
        if (command == null) {
            sender.printMessage(new Message(Category.WARNING, "Command not found: " + request.command()));

            return;
        }

        User userByToken = jwtGenerator.validateToken(request.token());
        if (userByToken == null && command.getType().isNeedAuth()) {
            sendResponse(new Response(false, "Ошибка авторизации"), address, port);
            return;
        }

        requestProcessorPool.execute(() -> {
            Response response = command.execute(request, this.collection, jwtGenerator.validateToken(request.token()));
            sendResponse(response, address, port);
        });
    }

    private boolean isValidCommand(Request request) {
        String command = request.command().trim();
        return !command.equalsIgnoreCase("execute_script") && !command.isEmpty();
    }


    private boolean isAddressBlocked(String ipAddress) {
        Long blockEndTime = blockedAddresses.get(ipAddress);
        return blockEndTime != null && System.currentTimeMillis() < blockEndTime;
    }

    private void blockAddress(String ipAddress) {
        blockedAddresses.put(ipAddress, System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(BLOCK_DURATION_MINUTES));
    }

    private void sendBlockedResponse(InetAddress address, int port) {
        sendResponse(new Response(false, "Ты ошибка "+ address.getHostAddress()), address, port);
    }

    public void sendResponse(Response response, InetAddress address, int port) {
        new Thread(() -> {
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
        }).start();
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