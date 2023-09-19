package Loaders;

import com.icsd.demo.models.UserModel;
import com.icsd.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ApplicationRunner implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        // Check if default users already exist to avoid duplication
        if (userRepository.count() == 0) {
            UserModel user1 = new UserModel();
            user1.setUsername("admin");
            user1.setPassword("admin");
            user1.setName("Panagiwtis");
            user1.setSurname("Papadakis");
            user1.setRole("admin");

            UserModel user2 = new UserModel();
            user2.setUsername("epimelitis");
            user2.setPassword("epimelitis");
            user2.setName("Nikolaos");
            user2.setSurname("Orfanos");
            user2.setRole("epimelitis");

            userRepository.save(user1);
            userRepository.save(user2);

            System.out.println("Default users saved.");
        } else {
            System.out.println("Default users already exist.");
        }
    }
}
