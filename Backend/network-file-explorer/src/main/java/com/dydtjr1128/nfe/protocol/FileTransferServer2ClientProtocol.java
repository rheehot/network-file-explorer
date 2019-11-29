package com.dydtjr1128.nfe.protocol;

import com.dydtjr1128.nfe.server.Client;
import com.dydtjr1128.nfe.protocol.core.BindingData;
import com.dydtjr1128.nfe.protocol.core.Protocol;
import lombok.NoArgsConstructor;

import java.nio.channels.AsynchronousSocketChannel;
@NoArgsConstructor
public class FileTransferServer2ClientProtocol extends Protocol {

    @Override
    public void executeProtocol(AsynchronousSocketChannel asc, BindingData bindingData) {

    }

    @Override
    public void executeProtocolToAdmin(Client client, BindingData bindingData) {
        System.out.println("여기 안들어옴");
    }
}