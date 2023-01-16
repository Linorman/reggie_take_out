package com.linorman.reggie_take_out;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 运行类
 * @author linorman
 * @since 2020-04-10
 */
@Slf4j
@SpringBootApplication
public class ReggieTakeOutApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReggieTakeOutApplication.class, args);
        log.info("Reggie Take Out Application Started");
    }

}
