package protocol;

import file.AsyncFileTransferFactory;
import protocol.core.BindingData;
import protocol.core.Protocol;

import java.io.IOException;
import java.nio.channels.AsynchronousSocketChannel;

public class FileTransferClient2ServerProtocol extends Protocol {

    @Override
    public void executeProtocol(AsynchronousSocketChannel asc, BindingData bindingData) throws IOException {
        System.out.println(bindingData.getPayload()+"@@@!@@@@@@@@@@@");
        AsyncFileTransferFactory.getInstance().getSender().send(bindingData.getPayload());
    }
}