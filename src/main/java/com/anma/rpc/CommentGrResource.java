package com.anma.rpc;

import com.anma.Comment;
import com.anma.proto.CommentRequest;
import com.anma.proto.CommentResponse;
import com.anma.proto.CommentService;
import io.quarkus.grpc.GrpcClient;
import io.smallrye.mutiny.Uni;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.awt.*;

@Path("/comments")
public class CommentGrResource {

    @GrpcClient
    CommentService commentService;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<CommentResponse> getCommentRpc(CommentRequest request) {
        return commentService.getComment(request);
    }

    @Path("/body")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<String> getCommentBodyRpc(CommentRequest request) {
        return commentService.getComment(request).onItem().transform(CommentResponse::getBody);
    }
}
