package org.esgi;

import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;

/**
 * Created by matpe on 30/05/2016.
 */

public class Attachment{
    public AsynchronousServerSocketChannel server;
    public AsynchronousSocketChannel client;
    public ByteBuffer buffer;
    public SocketAddress clientAddress;
}