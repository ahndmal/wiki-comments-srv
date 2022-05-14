package com.anma.rpc;

import com.anma.proto.CommentRequest;
import com.anma.proto.CommentResponse;
import com.anma.proto.CommentService;
import com.anma.repo.CommentRepo;
import io.quarkus.grpc.GrpcService;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

import javax.inject.Inject;

@GrpcService
public class CommentRpcService implements CommentService {

    @Inject
    CommentRepo commentRepo;

    @Override
    public Uni<CommentResponse> getComment(CommentRequest request) {

        var comment = commentRepo.findById((long)request.getId()).await().indefinitely();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return Uni.createFrom().item(() ->
                CommentResponse.newBuilder()
                        .setId(comment.getId())
                        .setBody(comment.getBody())
                        .setParentId(comment.getParentId())
                        .build()
        );
    }

    @Override
    public Multi<CommentResponse> streamComments(CommentRequest request) {
//        return Multi.createFrom().generator();
        return null;
    }
}

