package io.coremaker.codechallenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class CodeChallengeApplication {

    public static void main(String[] args) {
        SpringApplication.run(CodeChallengeApplication.class, args);
    }

}
