package com.anma;

import com.anma.repo.CommentRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.smallrye.mutiny.Uni;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/app/comments")
@ApplicationScoped
public class CommSocket {

    Logger log = LoggerFactory.getLogger(CommSocket.class);
    Map<String, Session> sessions = new ConcurrentHashMap<>();

    @Inject
    CommentRepo commentRepo;

    public CommSocket(CommentRepo commentRepo) {
        this.commentRepo = commentRepo;
    }

    @OnOpen
//    @Blocking
    public void onOpen(Session session) {
        sessions.put("username", session);
        broadcast(">>> hello from server!");
//        sendbynaryObject();
        sendAsJson(commentRepo.findById(998L));
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove("username", session);
    }

    @OnError
    public void onError(Session session, @PathParam("username") String username, Throwable throwable) {
//        sessions.remove(username);
        broadcast("User " + username + " left on error: " + throwable);
        throwable.printStackTrace();
    }

    @OnMessage
    public void onMessage(String message, @PathParam("username") String username) {
        if (message.equalsIgnoreCase("_ready_")) {
            broadcast("User " + username + " joined");
        } else {
            broadcast(">> " + username + ": " + message);
        }
    }


    private void broadcast(String message) {
        sessions.values().forEach(s -> {
            s.getAsyncRemote().sendObject(message, result ->  {
                if (result.getException() != null) {
                    System.out.println("Unable to send message: " + result.getException());
                }
            });
//            var comm1 = commentRepo.findById(993L);
//            s.getAsyncRemote().sendObject(comm1, res -> {
//                if (res.getException() != null) {
//                    System.out.println("Unable to send message: " + res.getException());
//                }
//            });
        });
    }

    private void sendbynaryObject() {
        byte[] commBytes = new byte[0];
        try {
//            commBytes = convertToBytes(commentRepo.findById(993L));
            commBytes = convertToBytes(new String("asdasdasdasdasd"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        var buff = ByteBuffer.wrap(commBytes).asReadOnlyBuffer();
        sessions.values().forEach(s -> s.getAsyncRemote().sendBinary(buff, res -> {
            if (res.getException() != null) {
                System.out.println("Unable to send message: " + res.getException());
            }
        }));
    }

    private void sendAsync() {
        sessions.values().forEach(session -> {
            commentRepo.findById(998L).onItem().invoke(item -> {
                session.getAsyncRemote().sendObject(item, sendResult -> {
                    if (sendResult.getException() != null) {
                        log.info(sendResult.getException().getMessage());
                    }
                });
            });
        });
    }

    private void sendAsJson(Uni<Comment> comment) {
        comment.map(c -> {
            ObjectMapper mapper = new ObjectMapper();
            try {
                var json = mapper.writeValueAsString(c);
                sessions.values().forEach(s -> {
                    s.getAsyncRemote().sendObject(json, result ->  {
                        if (result.getException() != null) {
                            System.out.println("Unable to send message: " + result.getException());
                        }
                    });
                });
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            return c;
        }).onItem().invoke((i) -> System.out.println(i)).subscribe();

    }


    private byte[] convertToBytes(Object object) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream out = new ObjectOutputStream(bos)) {
            out.writeObject(object);
            return bos.toByteArray();
        }
    }

}
