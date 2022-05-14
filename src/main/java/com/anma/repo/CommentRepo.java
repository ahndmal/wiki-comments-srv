package com.anma.repo;

import com.anma.Comment;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

@ApplicationScoped
public class CommentRepo implements PanacheRepository<Comment> {

    @Transactional
    Uni<Comment> findByAuthor(long authorId) {
        return find("authorId", authorId).firstResult();
    }

}
