package com.dydtjr1128.nfe.server;

import com.dydtjr1128.nfe.server.model.AdminMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


public class ClientManager {
    private static final Logger logger = LoggerFactory.getLogger(ClientManager.class);
    private static ClientManager INSTANCE = null;
    public final List<Client> clientsVector;//If want to use many client one Ip
    public final Map<String, Client> clientsHashMap;//If want to use one client one IP


    public static ClientManager getInstance() {
        return LazyHolder.INSTANCE;
    }

    public void addClient(Client client) {
        String clientIP = client.getClientIP();
        clientsVector.add(client);
        clientsHashMap.put(clientIP, client);
        AdminWebSocketManager.getInstance().writeToAdminPage(new AdminMessage(AdminMessage.ADD, clientIP));
    }

    public void removeClient(Client client) {
        String clientIP = client.getClientIP();
        clientsVector.remove(client);
        clientsHashMap.remove(clientIP);
        AdminWebSocketManager.getInstance().writeToAdminPage(new AdminMessage(AdminMessage.REMOVE, clientIP));
    }

    public int getClientCount() {
        return clientsVector.size();
    }

    private static class LazyHolder {
        private static final ClientManager INSTANCE = new ClientManager();
    }

    private ClientManager() {
        clientsVector = Collections.synchronizedList(new Vector<>(1024));
        clientsHashMap = Collections.synchronizedMap(new HashMap<>());
    }

}


/*
package com.dydtjr1128.nfe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ClientManager {
    private static final Logger logger = LoggerFactory.getLogger(ClientManager.class);
    private static ClientManager INSTANCE = null;
    public BlockingQueue<SocketChannel> socketChannelBlockingQueue;
    public HashMap<String, SocketChannel> clientHashMap;


    private static class LazyHolder {
        private static final ClientManager INSTANCE = new ClientManager();
    }

    public static ClientManager getInstance() {
        return LazyHolder.INSTANCE;
    }

    private ClientManager() {
        socketChannelBlockingQueue = new ArrayBlockingQueue<>(1024);
        clientHashMap = new HashMap<>();
    }

    public void addSocketChannel(SocketChannel socketChannel) {
        try {
            String clientIP = ((InetSocketAddress) socketChannel.getRemoteAddress()).getHostName();
            socketChannelBlockingQueue.put(socketChannel);
            clientHashMap.put(clientIP, socketChannel);
        } catch (IOException | InterruptedException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }

    }

}
*/