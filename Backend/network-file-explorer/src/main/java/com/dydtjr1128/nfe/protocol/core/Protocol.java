package com.dydtjr1128.nfe.protocol.core;

import com.dydtjr1128.nfe.network.Client;
import com.google.gson.Gson;

import java.nio.channels.AsynchronousSocketChannel;

public abstract class Protocol {
    private Gson gson;
    public Protocol(){
        gson = new Gson();;
    }

    public Gson getGson(){return gson;}
    public abstract void executeProtocol(AsynchronousSocketChannel asc, BindingData bindingData);
    public abstract void executeProtocolToAdmin(Client client, BindingData bindingData);
}