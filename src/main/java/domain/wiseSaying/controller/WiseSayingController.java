package domain.wiseSaying.controller;


import com.back.Rq;
import domain.wiseSaying.entity.WiseSaying;
import domain.wiseSaying.service.WiseSayingService;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class WiseSayingController {
    private final WiseSayingService wiseSayingService;
    private final Scanner sc;

    public WiseSayingController(WiseSayingService wiseSayingService, Scanner sc) {
        this.wiseSayingService = wiseSayingService;
        this.sc = sc;
    }

    public void actionWrite() {
        System.out.print("명언 : ");
        String content = sc.nextLine().trim();
        System.out.print("작가 : ");
        String author = sc.nextLine().trim();

        int currentId = wiseSayingService.register(content, author);
        System.out.println("%d번 명언이 등록되었습니다.".formatted(currentId));
    }

    public void actionDelete(Rq rq) {
        int del_idx = rq.getParamAsInt("id", -1);
        if (del_idx == -1) {
            System.out.println("id를 정확히 입력해주세요.");
            return;
        }
        boolean isDeleted = wiseSayingService.delete(del_idx);
        if (isDeleted) {
            System.out.println("%d번 명언이 삭제되었습니다.".formatted(del_idx));
        }
        else {System.out.println("%d번 명언은 존재하지 않습니다.".formatted(del_idx));}
    }

    public void actionList() {
        System.out.println("번호 / 작가 / 명언");
        System.out.println("----------------------");
        List<WiseSaying> wisesayings;
        wisesayings = wiseSayingService.wiseSayingList();

        wisesayings.reversed().stream()
                .map(wisesaying -> "%d / %s / %s".formatted(
                        wisesaying.getId(),
                        wisesaying.getAuthor(),
                        wisesaying.getContent()
                ))
                .forEach(System.out::println);
    }

    public void actionModify(Rq rq) {
        int modify_idx = rq.getParamAsInt("id", -1);
        if (modify_idx == -1) {
            System.out.println("id를 정확히 입력해주세요.");
            return;
        }
        Optional<WiseSaying> beforeModi = wiseSayingService.beforeModify(modify_idx);
        if(beforeModi.isPresent()) {
            WiseSaying wisesaying = beforeModi.get();
            System.out.println("명언(기존) : %s".formatted(wisesaying.getContent()));
            System.out.print("명언 : ");
            wisesaying.setContent(sc.nextLine().trim());

            System.out.println("작가(기존) : %s".formatted(wisesaying.getAuthor()));
            System.out.print("작가 : ");
            wisesaying.setAuthor(sc.nextLine().trim());
            wiseSayingService.afterModify(wisesaying);
        }
        else {
            System.out.println("%d번 명언은 존재하지 않습니다.".formatted(modify_idx));
        }
    }

    public void actionBuild() {
        boolean isBuild = wiseSayingService.doBuild();
        if (isBuild) {
            System.out.println("data.json 파일의 내용이 갱신되었습니다.");
        }
    }
}
