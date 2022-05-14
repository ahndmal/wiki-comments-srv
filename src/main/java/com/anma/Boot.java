package com.anma;

import com.anma.repo.CommentRepo;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

@ApplicationScoped
public class Boot {

    private static final Logger LOGGER = Logger.getLogger("ListenerBean");
    @Inject
    CommentRepo commentRepo;


    public Boot(CommentRepo commentRepo) {
        this.commentRepo = commentRepo;
    }

    void onStart(@Observes StartupEvent ev) {
        LOGGER.info(" >>>>>>>>>>>>>>> The application is starting...");
        System.out.println(commentRepo.findAll().list());

    }

    void onStop(@Observes ShutdownEvent ev) {
        LOGGER.info("The application is stopping...");
    }
}
