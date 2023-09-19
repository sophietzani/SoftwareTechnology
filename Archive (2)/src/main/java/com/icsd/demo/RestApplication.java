package com.icsd.demo;

import Loaders.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RestApplication extends SpringBootServletInitializer implements CommandLineRunner {

    public static void main(String[] args) {
        new RestApplication().configure(new SpringApplicationBuilder(RestApplication.class)).run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Application Started");
    }

    @Bean
    public ApplicationRunner applicationStartupRunner() {
        return new ApplicationRunner();
    }

}
