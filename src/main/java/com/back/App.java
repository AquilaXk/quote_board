package com.back;

import domain.system.controller.SystemController;
import domain.wiseSaying.controller.WiseSayingController;
import domain.wiseSaying.entity.WiseSaying;
import domain.wiseSaying.repository.WiseSayingRepository;
import domain.wiseSaying.service.WiseSayingService;


import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class App {
    private final WiseSayingRepository wiseSayingRepository;
    private final WiseSayingService wiseSayingService;
    private final WiseSayingController wiseSayingController;
    private final SystemController systemController;
    private final Scanner sc = new Scanner(System.in);
    private boolean status = true;

    public App() {
        this.wiseSayingRepository = new WiseSayingRepository("db/wiseSaying");
        this.wiseSayingService = new WiseSayingService(wiseSayingRepository);
        this.wiseSayingController = new WiseSayingController(wiseSayingService);
        this.systemController = new SystemController();
    }


    void run() {

        System.out.println("== 명언 앱 ==");

        while(status) {
            System.out.print("명령) ");
            String input = sc.nextLine().trim();
            Rq rq = new Rq(input);

            switch (rq.getActionName()) {
                case "등록":
                    actionWrite();
                    break;
                case "삭제":
                    actionDelete(rq);
                    break;
                case "목록":
                    actionList();
                    break;
                case "수정":
                    actionModify(rq);
                    break;
                case "종료":
                    actionExit();
                    break;
                case "빌드":
                    actionBuild();
                    break;
                default:
                    System.out.println("알 수 없는 명령어입니다.");
                    break;
            }
        }
        sc.close();
    }

    private void actionWrite() {
        System.out.print("명언 : ");
        String content = sc.nextLine().trim();
        System.out.print("작가 : ");
        String author = sc.nextLine().trim();

        int currentId = this.wiseSayingController.doRegist(content, author);
        System.out.println("%d번 명언이 등록되었습니다.".formatted(currentId));
    }

    private void actionDelete(Rq rq) {
        int del_idx = rq.getParamAsInt("id", -1);
        if (del_idx == -1) {
            System.out.println("id를 정확히 입력해주세요.");
            return;
        }
        boolean isDeleted = this.wiseSayingController.doDelete(del_idx);
        if (isDeleted) {
            System.out.println("%d번 명언이 삭제되었습니다.".formatted(del_idx));
        }
        else {System.out.println("%d번 명언은 존재하지 않습니다.".formatted(del_idx));}
    }

    private void actionList() {
        System.out.println("번호 / 작가 / 명언");
        System.out.println("----------------------");
        List<WiseSaying> wisesayings;
        wisesayings = this.wiseSayingController.doMakeList();
        wisesayings.stream()
                .map(wisesaying -> "%d / %s / %s".formatted(
                        wisesaying.getId(),
                        wisesaying.getAuthor(),
                        wisesaying.getContent()
                ))
                .forEach(System.out::println);
    }

    private void actionModify(Rq rq) {
        int modi_idx = rq.getParamAsInt("id", -1);
        if (modi_idx == -1) {
            System.out.println("id를 정확히 입력해주세요.");
            return;
        }
        Optional<WiseSaying> beforModi = this.wiseSayingController.modifyRequest(modi_idx);
        if(beforModi.isPresent()) {
            WiseSaying wisesaying = beforModi.get();
            System.out.println("명언(기존) : %s".formatted(wisesaying.getContent()));
            System.out.print("명언 : ");
            wisesaying.setContent(sc.nextLine().trim());

            System.out.println("작가(기존) : %s".formatted(wisesaying.getAuthor()));
            System.out.print("작가 : ");
            wisesaying.setAuthor(sc.nextLine().trim());
            this.wiseSayingController.modifyUploadRequest(wisesaying);
        }
        else {
            System.out.println("%d번 명언은 존재하지 않습니다.".formatted(modi_idx));
        }
    }

    private void actionBuild() {
        boolean isSuccess = this.wiseSayingController.buildRequest();
        if (isSuccess) {
            System.out.println("data.json 파일의 내용이 갱신되었습니다.");
        }
    }

    private void actionExit() {
        status = this.systemController.exit();
    }
}
