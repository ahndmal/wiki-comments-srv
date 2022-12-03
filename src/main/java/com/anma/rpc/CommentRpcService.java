package com.anma.rpc;

import com.anma.Comment;
import com.anma.proto.CommentRequest;
import com.anma.proto.CommentResponse;
import com.anma.proto.CommentService;
import com.anma.repo.CommentRepo;
import io.quarkus.grpc.GrpcService;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Optional;

@GrpcService
public class CommentRpcService implements CommentService {
    private final Logger LOG = LoggerFactory.getLogger(CommentRpcService.class);
    @Inject
    CommentRepo commentRepo;

    @Override
    public Uni<CommentResponse> getComment(CommentRequest request) {

        System.out.format(">> getComment :: ID is %d", request.getId());

        var comment = commentRepo.findById(request.getId());
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
                .onItem().ifNull().failWith(() -> new Exception("Error"))
                .onFailure().invoke(fail -> System.out.println(fail));

    }

    @Override
    public Multi<CommentResponse> streamComments(CommentRequest request) {

        return Multi.createFrom().item(CommentResponse.newBuilder().setBody("aaa").build());    // todo
    }
}

