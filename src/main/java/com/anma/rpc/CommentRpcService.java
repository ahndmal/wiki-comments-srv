package com.anma.rpc;

import com.anma.Comment;
import com.anma.proto.CommentByBody;
import com.anma.proto.CommentRequest;
import com.anma.proto.CommentResponse;
import com.anma.proto.CommentService;
import com.anma.repo.CommentRepo;
import io.quarkus.grpc.GrpcService;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.stream.StreamSupport;

/*
   Mutiny or default gRPC
   https://quarkus.io/guides/grpc-service-implementation#implementing-a-service-with-the-default-grpc-api
*/

@GrpcService
public class CommentRpcService implements CommentService {
    private final Logger LOG = LoggerFactory.getLogger(CommentRpcService.class);
    private final CommentRepo commentRepo;
    private final PgPool client;

    @Inject
    public CommentRpcService(CommentRepo commentRepo, PgPool client) {
        this.commentRepo = commentRepo;
        this.client = client;
    }

    @Override
    public Uni<CommentResponse> getComment(CommentRequest request) {
        System.out.format(">> getComment :: ID is %d", request.getId());
        Uni<Comment> comment = commentRepo.findById(request.getId());
        LOG.info(">>> getting comment");
        LOG.info(comment.toString());

//        try {
//            var baos = new ByteArrayOutputStream();
//            var oos = new ObjectOutputStream(baos);
//            oos.writeObject(comment);
//            oos.flush();
//            byte[] bytes = baos.toByteArray();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        return comment.onItem().ifNotNull().transform(c ->
                CommentResponse.newBuilder()
                        .setId(c.getId())
//                        .setParentId(c.getParentId())
                        .setBody(c.getBody())
                        .build())
                .onItem().ifNull().failWith(() -> new Exception("Error when getting comment with ID " + request.getId()))
                .onFailure().invoke(fail -> System.out.println(fail));
    }

    @Override
    public Multi<CommentResponse> streamComments(CommentRequest request) {
        return client.query("select * from comments")
                .execute()
                .onItem().transformToMulti(
                    rs -> Multi.createFrom().items(() -> StreamSupport.stream(rs.spliterator(), false))
                ).map(row -> CommentResponse.newBuilder()
                        .setId(row.getLong("id"))
                        .setBody(row.getString("body"))
                        .setTitle(row.getString("title"))
                        .setUserId(row.getLong("author_id"))
                        .setParentId(row.getLong("parent_id"))
                        .build());
//        return Multi.createFrom().items(commentRepo.findAll())
////                .select().distinct()
//                .onItem().transform(pq -> pq.stream().map(c -> ))

//        return Multi.createFrom().item(CommentResponse.newBuilder().setBody("aaa").build());    // todo
    }

    @Override
    public Multi<CommentResponse> getCommentsByBody(CommentByBody request) {
        return null;
    }
}

