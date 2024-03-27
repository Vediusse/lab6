package viancis.lab6.client;

import viancis.lab6.common.communication.Response;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.net.DatagramPacket;

public class Handler {

    private static final int BUFFER_SIZE = 1024;

    private final DatagramSocket datagramSocket;
    private final InetAddress serverAddress;
    private int serverPort;

    public Handler(String address) throws UnknownHostException, SocketException {
        datagramSocket = new DatagramSocket();
        serverAddress = InetAddress.getByName(address);
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public void sendData(byte[] data) throws IOException {
        DatagramPacket sendPacket = new DatagramPacket(data, data.length, serverAddress, serverPort);
        datagramSocket.send(sendPacket);
    }

    public Response receiveResponse() throws IOException, ClassNotFoundException {
        byte[] receiveBuffer = new byte[BUFFER_SIZE];
        DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
        datagramSocket.receive(receivePacket);

        // Deserialize the received Response object
        ByteArrayInputStream byteStream = new ByteArrayInputStream(receivePacket.getData());
        ObjectInputStream objectStream = new ObjectInputStream(byteStream);
        return (Response) objectStream.readObject();
    }

    public void close() {
        datagramSocket.close();
    }
}

