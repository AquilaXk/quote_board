package domain.wiseSaying.controller;


import domain.wiseSaying.entity.WiseSaying;
import domain.wiseSaying.service.WiseSayingService;

import java.util.List;
import java.util.Optional;

public class WiseSayingController {
    private final WiseSayingService wiseSayingService;

    public WiseSayingController(WiseSayingService wiseSayingService) {
        this.wiseSayingService = wiseSayingService;
    }


    public int doRegist(String content, String author) {
        int currentId = wiseSayingService.register(content, author);
        return currentId;
    }

    public boolean doDelete(int del_idx) {
        boolean isDeleted = wiseSayingService.delete(del_idx);
        return isDeleted;
    }
    public List<WiseSaying> doMakeList() {
        return wiseSayingService.wiseSayingList();
    }

    public Optional<WiseSaying> modifyRequest(int modi_idx) {
        return wiseSayingService.beforeModify(modi_idx);
    }

    public void modifyUploadRequest(WiseSaying wisesaying) {
        wiseSayingService.afterModify(wisesaying);
    }

    public boolean buildRequest() {
        return wiseSayingService.doBuild();
    }
}
