package academy.devdojo.controller;

import academy.devdojo.domain.Anime;
import academy.devdojo.mapper.AnimeMapper;
import academy.devdojo.request.AnimePostRequest;
import academy.devdojo.request.AnimePutRequest;
import academy.devdojo.response.AnimeGetResponse;
import academy.devdojo.response.AnimePostResponse;
import academy.devdojo.service.AnimeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("v1/animes")
@Slf4j
public class AnimeController {
    private static final AnimeMapper MAPPER = AnimeMapper.INSTANCE;
    private AnimeService service;

    public AnimeController() {
        this.service = new AnimeService();
    }

    @GetMapping("virtualThreads")
    public List<String> listAllVirtualThreads() throws InterruptedException {
        log.info(Thread.currentThread().getName());
        TimeUnit.SECONDS.sleep(1);
        return List.of("Naruto", "One Piece");
    }

    @GetMapping
    public ResponseEntity<List<AnimeGetResponse>> listAll() {
        log.debug("Request received to list all animes");

        var anime = service.findAll();
        var response = MAPPER.toAnimeGetResponseList(anime);

        return ResponseEntity.ok(response);
    }

    @GetMapping("filter")
    public ResponseEntity<List<AnimeGetResponse>> filter(@RequestParam String name){
        log.debug("Request received to list all animes, param name '{}'", name);

        var anime = service.findByName(name);
        var response = MAPPER.toAnimeGetResponseList(anime);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnimeGetResponse> findById(@PathVariable Long id){
        log.debug("Request to find anime by id: {}", id);

        var anime = service.findById(id);
        var response = MAPPER.toAnimeGetResponse(anime);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<AnimePostResponse> save(@RequestBody AnimePostRequest animePostRequest){
        log.debug("Request to save anime: {}", animePostRequest);

        var anime = MAPPER.toAnime(animePostRequest);
        service.save(anime);
        var response = MAPPER.toAnimePostResponse(anime);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){
        log.debug("Request to delete anime by id: {}", id);

        var anime = service.findById(id);
        service.delete(anime.getId());

        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody AnimePutRequest animePutRequest){
        log.debug("Request to update anime: {}", animePutRequest);

        var anime = MAPPER.toAnime(animePutRequest);
        service.update(anime);

        return ResponseEntity.noContent().build();
    }
}
