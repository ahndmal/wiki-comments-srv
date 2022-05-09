package org.anma.repo;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.anma.Comment;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class CommentRepo implements PanacheRepository<Comment> {

    List<Comment> findByAuthor(long authorId) {
        return find("authorId", authorId).firstResult();
    }

}
