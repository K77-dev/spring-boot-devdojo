package academy.devdojo.repository;

import academy.devdojo.domain.Anime;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AnimeHardCodedRepository {
    private static final List<Anime> ANIMES = new ArrayList<>();

    static {
        ANIMES.addAll(
                List.of(
                        Anime.builder().id(1L).name("Naturo").build(),
                        Anime.builder().id(2L).name("Goku").build(),
                        Anime.builder().id(3L).name("One Piece").build()
                )
        );
    }

    public List<Anime> findAll(){
        return ANIMES;
    }

    public Optional<Anime> findById(Long id){
        return ANIMES.stream()
                .filter( n -> n.getId().equals(id))
                .findFirst();
    }

    public List<Anime> findByName(String name){
        return ANIMES.stream()
                .filter(n -> n.getName().equalsIgnoreCase(name))
                .toList();
    }

    public Anime save(Anime anime){
        ANIMES.add(anime);
        return anime;
    }

    public void delete(Anime anime){
        ANIMES.remove(anime);
    }

    public void update(Anime anime){
        delete(anime);
        save(anime);
    }
}
