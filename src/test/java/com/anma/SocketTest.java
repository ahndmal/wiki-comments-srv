//package com.anma;
//
//import io.quarkus.test.common.http.TestHTTPResource;
//import io.quarkus.test.junit.QuarkusTest;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//
//import javax.websocket.*;
//import javax.ws.rs.client.Client;
//import java.net.URI;
//import java.util.concurrent.LinkedBlockingDeque;
//import java.util.concurrent.TimeUnit;
//
//@QuarkusTest
//public class SocketTest {
//
//    private static final LinkedBlockingDeque<String> MESSAGES = new LinkedBlockingDeque<>();
//
//    @TestHTTPResource("/app/comments/petro")
//    URI uri;
//
//    @Test
//    public void testWebsocketChat() throws Exception {
//        try (Session session = ContainerProvider.getWebSocketContainer().connectToServer(Client.class, uri)) {
//            Assertions.assertEquals("CONNECT", MESSAGES.poll(10, TimeUnit.SECONDS));
//            Assertions.assertEquals("User petro joined", MESSAGES.poll(10, TimeUnit.SECONDS));
//            session.getAsyncRemote().sendText("hello world");
//            Assertions.assertEquals(">> stu: hello world", MESSAGES.poll(10, TimeUnit.SECONDS));
//        }
//    }
//
//    @ClientEndpoint
//    public static class Client {
//
//        @OnOpen
//        public void open(Session session) {
//            MESSAGES.add("CONNECT");
//            // Send a message to indicate that we are ready,
//            // as the message handler may not be registered immediately after this callback.
//            session.getAsyncRemote().sendText("_ready_");
//        }
//
//        @OnMessage
//        void message(String msg) {
//            MESSAGES.add(msg);
//        }
//
//    }
//}
