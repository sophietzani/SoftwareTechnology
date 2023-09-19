package com.icsd.demo;

import com.icsd.demo.controllers.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.icsd.demo.controllers.CommentController;
import com.icsd.demo.controllers.NewsController;
import com.icsd.demo.controllers.TopicController;
import com.icsd.demo.controllers.UserController;
import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Path;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

@Component
@ApplicationPath("/api")
@Path("/")
@PermitAll
public class JerseyConfig extends ResourceConfig {

    
    public JerseyConfig(ObjectMapper objectMapper) {
        packages("com.icsd.demo");
        register(UserController.class);
        register(CommentController.class);
        register(NewsController.class);
        register(TopicController.class);
        register(SecurityFilter.class);
        register(TokenService.class);
        
        register(new ObjectMapperContextResolver(objectMapper));
    }

    @Provider
    public static class ObjectMapperContextResolver implements ContextResolver<ObjectMapper> {

        private final ObjectMapper mapper;

        public ObjectMapperContextResolver(ObjectMapper mapper) {
            this.mapper = mapper;
        }

        @Override
        public ObjectMapper getContext(Class<?> type) {
            return mapper;
        }

    }

    @PostConstruct
    public void init() {
        
    }


}
