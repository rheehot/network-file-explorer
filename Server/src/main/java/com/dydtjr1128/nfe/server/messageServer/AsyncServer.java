package com.dydtjr1128.nfe.server.messageServer;

import com.dydtjr1128.nfe.admin.service.ApplicationContextProvider;
import com.dydtjr1128.nfe.server.config.Config;
import com.dydtjr1128.nfe.server.connection.ConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.Executors;

public class AsyncServer implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(AsyncServer.class);
    private final AsynchronousServerSocketChannel assc;
    private final AsynchronousChannelGroup channelGroup;
    private ConnectionManager clientManager;

    public AsyncServer() throws IOException {
        channelGroup = AsynchronousChannelGroup.withFixedThreadPool(Config.DEFAULT_THREAD_POOL_COUNT, Executors.defaultThreadFactory());
        assc = createAsynchronousServerSocketChannel();
        clientManager = ApplicationContextProvider.getApplicationContext().getBean(ConnectionManager.class);
        logger.debug("[Finish server setting with " + Config.DEFAULT_THREAD_POOL_COUNT + " thread in thread pool]");
    }

    @Override
    public void run() {
        logger.debug("[New client waiting...]");
        assc.accept(null, new CompletionHandler<AsynchronousSocketChannel, Void>() {
            @Override
            public void completed(AsynchronousSocketChannel result, Void attachment) {
                // 비동기 소켓 연결 // accept the next connection
                if (assc.isOpen())
                    assc.accept(null, this);
                try {
                    handleNewConnection(result);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(Throwable exc, Void attachment) {
            }
        });
    }

    private AsynchronousServerSocketChannel createAsynchronousServerSocketChannel() throws IOException {
        final AsynchronousServerSocketChannel serverSocketChannel = AsynchronousServerSocketChannel.open(channelGroup);
        serverSocketChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
        serverSocketChannel.bind(new InetSocketAddress(Config.ASYNC_SERVER_PORT));
        return serverSocketChannel;
    }

    private void handleNewConnection(AsynchronousSocketChannel channel) throws IOException {
        Connection connection = new Connection(channel);
        logger.debug("[New client connected] : " + connection.getClientIP());
        try {
            channel.setOption(StandardSocketOptions.TCP_NODELAY, true);
        } catch (IOException e) {
            logger.debug("", e);
            // ignore
        }
        clientManager.addClient(connection);
        connection.run();
    }
}
