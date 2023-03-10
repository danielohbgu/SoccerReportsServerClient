package bgu.spl.net.srv;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.api.MessagingProtocol;
import bgu.spl.net.api.StompMessagingProtocol;
import bgu.spl.net.impl.stomp.StompBaseServer;
import bgu.spl.net.impl.stomp.StompBlockingConnectionHandler;
import bgu.spl.net.impl.stomp.StompMessageEncoderDecoder;
import bgu.spl.net.impl.stomp.StompReactor;

import java.io.Closeable;
import java.util.function.Supplier;

public interface Server<T> extends Closeable {

    /**
     * The main loop of the server, Starts listening and handling new clients.
     */
    void serve();

    /**
     *This function returns a new instance of a thread per client pattern server
     * @param port The port for the server socket
     * @param protocolFactory A factory that creats new MessagingProtocols
     * @param encoderDecoderFactory A factory that creats new MessageEncoderDecoder
     * @param <T> The Message Object for the protocol
     * @return A new Thread per client server
     */
    public static <T> Server<T>  threadPerClient(
            int port,
            Supplier<MessagingProtocol<T> > protocolFactory,
            Supplier<MessageEncoderDecoder<T> > encoderDecoderFactory) {

        return new BaseServer<T>(port, protocolFactory, encoderDecoderFactory) {
            @Override
            protected void execute(BlockingConnectionHandler<T>  handler) {
                new Thread(handler).start();
            }
        };

    }

    public static <T> Server<T>  stompThreadPerClient(
            int port,
            Supplier<StompMessagingProtocol<T> > protocolFactory,
            Supplier<MessageEncoderDecoder<T> > encoderDecoderFactory) {

        return new StompBaseServer<T>(port, protocolFactory, encoderDecoderFactory) {
            @Override
            protected void execute(StompBlockingConnectionHandler<T>  handler) {
                new Thread(handler).start();
            }
        };
    }

    /**
     * This function returns a new instance of a reactor pattern server
     * @param nthreads Number of threads available for protocol processing
     * @param port The port for the server socket
     * @param protocolFactory A factory that creats new MessagingProtocols
     * @param encoderDecoderFactory A factory that creats new MessageEncoderDecoder
     * @param <T> The Message Object for the protocol
     * @return A new reactor server
     */
    public static <T> Server<T> reactor(
            int nthreads,
            int port,
            Supplier<MessagingProtocol<T>> protocolFactory,
            Supplier<MessageEncoderDecoder<T>> encoderDecoderFactory) {
        return new Reactor<T>(nthreads, port, protocolFactory, encoderDecoderFactory);
    }

    /**
     * This function returns a new instance of a stomp reactor pattern server
     * @param nthreads Number of threads available for protocol processing
     * @param port The port for the server socket
     * @param protocolFactory A factory that creats new MessagingProtocols
     * @param encoderDecoderFactory A factory that creats new MessageEncoderDecoder
     * @param <T> The Message Object for the protocol
     * @return A new stomp reactor server
     */
    public static <T> Server<T> stompReactor(
            int nthreads,
            int port,
            Supplier<StompMessagingProtocol<T>> protocolFactory,
            Supplier<MessageEncoderDecoder<T>> encoderDecoderFactory) {
        return new StompReactor<T>(nthreads, port, protocolFactory, encoderDecoderFactory);
    }

}
