package academy.devdojo.controller;

import academy.devdojo.domain.Anime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("v1/animes")
@Slf4j
public class AnimeController {

    @GetMapping("virtualThreads")
    public List<String> listAllVirtualThreads() throws InterruptedException {
        log.info(Thread.currentThread().getName());
        TimeUnit.SECONDS.sleep(1);
        return List.of("Naruto", "One Piece");
    }

    @GetMapping
    public List<Anime> listAll() {
        return Anime.getAnimes();
    }

    @GetMapping("filter")
    public List<Anime> filter(@RequestParam String name){
        return Anime.getAnimes().stream()
                .filter(n -> n.getName().equalsIgnoreCase(name))
                .toList();
    }

    @GetMapping("/{id}")
    public Anime findById(@PathVariable Long id){
        return Anime.getAnimes().stream()
                .filter( n -> n.getId().equals(id))
                .findFirst().orElse(null);
    }
}
