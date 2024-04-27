package org.rayvivek881.megamart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;


@SpringBootApplication
@EnableMongoAuditing
public class MegaMartApplication {
  public static void main(String[] args) {
    SpringApplication.run(MegaMartApplication.class, args);
  }
}
