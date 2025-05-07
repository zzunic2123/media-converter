package com.zvonimir.imageconverter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ImageConverterApplication {

    public static void main(String[] args) {
        System.setProperty("jave.ffmpeg.executable", "ffmpeg");
        SpringApplication.run(ImageConverterApplication.class, args);
    }

}
