package bgu.spl.net.impl.stomp;

import bgu.spl.net.srv.Server;

public class StompServer {

    public static void main(String[] args) {
        Server.stompThreadPerClient(
                7777, //port
                () -> new StompMessegingProtocolImpl(), //protocol factory
                StompMessageEncoderDecoder::new //message encoder decoder factory
        ).serve();

        // Server.stompReactor(
        //         Runtime.getRuntime().availableProcessors(),
        //         7777, //port
        //         () -> new StompMessegingProtocolImpl<>(), //protocol factory
        //         StompMessageEncoderDecoder::new //message encoder decoder factory
        // ).serve();
    }
}
