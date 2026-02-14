package academy.devdojo.controller;

import academy.devdojo.domain.Anime;
import academy.devdojo.mapper.AnimeMapper;
import academy.devdojo.request.AnimePostRequest;
import academy.devdojo.response.AnimeGetResponse;
import academy.devdojo.response.AnimePostResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("v1/animes")
@Slf4j
public class AnimeController {

    private static final AnimeMapper MAPPER = AnimeMapper.INSTANCE;

    @GetMapping("virtualThreads")
    public List<String> listAllVirtualThreads() throws InterruptedException {
        log.info(Thread.currentThread().getName());
        TimeUnit.SECONDS.sleep(1);
        return List.of("Naruto", "One Piece");
    }

    @GetMapping
    public ResponseEntity<List<AnimeGetResponse>> listAll() {
        log.debug("Request received to list all animes");

        var response = Anime.getAnimes()
                .stream()
                .map(MAPPER::toAnimeGetResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("filter")
    public ResponseEntity<AnimeGetResponse> filter(@RequestParam String name){
        log.debug("Request received to list all animes, param name '{}'", name);

        var response = Anime.getAnimes().stream()
                .filter(n -> n.getName().equalsIgnoreCase(name))
                .findFirst()
                .map(MAPPER::toAnimeGetResponse)
                .orElse(null);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnimeGetResponse> findById(@PathVariable Long id){
        log.debug("Request to find anime by id: {}", id);

        var response = Anime.getAnimes().stream()
                .filter( n -> n.getId().equals(id))
                .findFirst()
                .map(MAPPER::toAnimeGetResponse)
                .orElse(null);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<AnimePostResponse> save(@RequestBody AnimePostRequest animePostRequest){
        log.debug("Request to save anime: {}", animePostRequest);

        var request = MAPPER.toAnimePostRequest(animePostRequest);
        var response = MAPPER.toAnimePostResponse(request);
        Anime.getAnimes().add(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
