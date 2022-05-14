package com.anma.rpc;

import com.anma.proto.CommentRequest;
import com.anma.proto.CommentResponse;
import com.anma.proto.CommentServiceGrpc;
import com.anma.repo.CommentRepo;
import io.grpc.stub.StreamObserver;
import io.quarkus.grpc.GrpcService;

import javax.inject.Inject;

@GrpcService
public class CommentRpcServiceBase extends CommentServiceGrpc.CommentServiceImplBase {

    @Inject
    CommentRepo commentRepo;

    @Override
    public void getComment(CommentRequest request, StreamObserver<CommentResponse> responseObserver) {

        var comment = commentRepo.findById(request.getId()).await().indefinitely();

        responseObserver.onNext(CommentResponse.newBuilder()
                .setId(comment.getId())
                .setBody(comment.getBody())
                .setParentId(comment.getParentId())
                .build()
        );
        responseObserver.onCompleted();
    }
}

