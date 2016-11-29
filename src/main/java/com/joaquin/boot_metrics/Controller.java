package com.joaquin.boot_metrics;

import org.springframework.web.bind.annotation.*;

import com.codahale.metrics.annotation.Timed;


@RestController
public class Controller {

    @Timed
    @RequestMapping("/hello")
    @ResponseBody
    String hello() {
        return "Hello World!";
    }

    @Timed
    @RequestMapping("/goodbye")
    @ResponseBody
    String goodbye() {
        return "Goodbye Cruel World!";
    }
    
}
