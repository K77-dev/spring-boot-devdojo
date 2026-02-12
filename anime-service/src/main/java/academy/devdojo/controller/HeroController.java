package academy.devdojo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("v1/heroes")
public class HeroController {
    private static final List<String> HEROES = List.of("Superman", "Batman", "Wonder Woman");

    @GetMapping
    public List<String> listAll() {
        return HEROES;
    }

    @GetMapping("filter")
    public List<String> listAllParam(@RequestParam(required = false, defaultValue = "") String name) {
        return HEROES.stream().filter(n -> n.equalsIgnoreCase(name)).toList();
    }

    @GetMapping("filterList")
    public List<String> listAllParamList(@RequestParam List<String> names) {
        return HEROES.stream().filter(names::contains).toList();
    }
}
