package domain.wiseSaying.service;

import domain.wiseSaying.entity.WiseSaying;
import domain.wiseSaying.repository.WiseSayingRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class WiseSayingService {
    private final WiseSayingRepository repository;

    public WiseSayingService(WiseSayingRepository repository) {
        this.repository = repository;
    }



    public int register(String content, String author) {
        int currentId = this.repository.getLastId();
        currentId++;
        WiseSaying wisesaying = new WiseSaying(currentId, content, author);
        LocalDateTime now = LocalDateTime.now();
        wisesaying.setCreateDate(now);
        wisesaying.setModifyDate(now);
        this.repository.save(wisesaying);
        this.repository.saveLastId(currentId);
        return currentId;
    }

    public boolean delete(int del_idx) {
        boolean isDeleted = this.repository.deleteData(del_idx);
        return isDeleted;
    }

    public List<WiseSaying> wiseSayingList() {
        List<WiseSaying> wisesayinglist = this.repository.getWiseSayingList();
        return wisesayinglist;
    }

    public Optional<WiseSaying> beforeModify(int modi_idx) {
        return this.repository.getBeforeModify(modi_idx);
    }

    public void afterModify(WiseSaying wisesaying) {
        wisesaying.setModifyDate(LocalDateTime.now());
        this.repository.save(wisesaying);
    }

    public boolean doBuild() {
        String jsonArray = this.repository.getWiseSayingList().stream()
                .map(this.repository::toMakeJson)
                .collect(Collectors.joining(",\n"));
        String finalJson = "[\n" + jsonArray + "\n]";
        return this.repository.saveBuildData(finalJson);
    }


}
