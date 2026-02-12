package academy.devdojo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "greetings")
public class HelloController {

    @GetMapping({"hello", "hi"})
    public String hello() {
        return "Hello DevDojo!";
    }
}
