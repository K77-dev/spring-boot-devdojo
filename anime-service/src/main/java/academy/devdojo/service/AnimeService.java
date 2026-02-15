package academy.devdojo.service;

import academy.devdojo.domain.Anime;
import academy.devdojo.repository.AnimeHardCodedRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

public class AnimeService {

    private AnimeHardCodedRepository repository;

    public AnimeService() {
        this.repository = new AnimeHardCodedRepository();
    }

    public List<Anime> findAll(){
        return repository.findAll();
    }

    public Anime findById(Long id){
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Anime not found!"));
    }

    public List<Anime> findByName(String name){
        return repository.findByName(name);
    }

    public Anime save(Anime anime){
        return repository.save(anime);
    }

    public void delete(Long id){
        repository.delete(findById(id));
    }

    public void update(Anime animeToUpdate){
        var anime = findById(animeToUpdate.getId());
         repository.update(animeToUpdate);
    }
}
