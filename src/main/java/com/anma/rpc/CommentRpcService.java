//package com.anma.rpc;
//
//import com.anma.Comment;
//import com.anma.proto.CommentRequest;
//import com.anma.proto.CommentResponse;
//import com.anma.proto.CommentService;
//import com.anma.repo.CommentRepo;
//import io.quarkus.grpc.GrpcService;
//import io.smallrye.mutiny.Multi;
//import io.smallrye.mutiny.Uni;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import javax.inject.Inject;
//import java.io.ByteArrayOutputStream;
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
//
//@GrpcService
//public class CommentRpcService implements CommentService {
//
//    private final Logger LOG = LoggerFactory.getLogger(CommentRpcService.class);
//    @Inject
//    CommentRepo commentRepo;
//
//    @Override
//    public Uni<CommentResponse> getComment(CommentRequest request) {
//
//        System.out.println(request.getId());
//
//        var comment = commentRepo.findById(request.getId());
//        LOG.info(">>> getting comment");
//        LOG.info(comment.toString());
//
////        try {
////            Thread.sleep(1000);
////        } catch (InterruptedException e) {
////            throw new RuntimeException(e);
////        }
//
////        try {
////            var baos = new ByteArrayOutputStream();
////            var oos = new ObjectOutputStream(baos);
////            oos.writeObject(comment);
////            oos.flush();
////            byte[] bytes = baos.toByteArray();
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
//
//
////        Uni.createFrom().item();
//
//        return Uni.createFrom().item(() ->
//                CommentResponse.newBuilder()
//                        .setId(comment.onItem().transform(Comment::getId).await().indefinitely())
//                        .setBody(comment.onItem().transform(c -> c.getBody()).await().indefinitely())
//                        .setParentId(comment.onItem().transform(c -> c.getParentId()).await().indefinitely())
//                        .build()
//        );
//    }
//
//    @Override
//    public Multi<CommentResponse> streamComments(CommentRequest request) {
////        return Multi.createFrom().generator();
//        return null;    // todo
//    }
//}
//
