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
        this.wiseSayingController = new WiseSayingController(wiseSayingService, sc);
        this.systemController = new SystemController();
    }


    void run() {

        System.out.println("== 명언 앱 ==");

        while(status) {
            System.out.print("명령) ");
            String input = sc.nextLine().trim();
            Rq rq = new Rq(input);

            switch (rq.getActionName()) {
                case "등록" -> wiseSayingController.actionWrite();
                case "삭제" -> wiseSayingController.actionDelete(rq);
                case "목록" -> wiseSayingController.actionList();
                case "수정" -> wiseSayingController.actionModify(rq);
                case "종료" -> status = systemController.actionExit();
                case "빌드" -> wiseSayingController.actionBuild();
                default -> System.out.println("알 수 없는 명령어입니다.");
            }
        }
        sc.close();
    }

}
