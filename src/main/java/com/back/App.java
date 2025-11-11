package com.back;


public class App {
    private boolean status = true;

    void run() {

        System.out.println("== 명언 앱 ==");

        while(status) {
            System.out.print("명령) ");
            String input = AppContext.sc.nextLine().trim();
            Rq rq = new Rq(input);

            switch (rq.getActionName()) {
                case "등록" -> AppContext.wiseSayingController.actionWrite();
                case "삭제" -> AppContext.wiseSayingController.actionDelete(rq);
                case "목록" -> AppContext.wiseSayingController.actionList();
                case "수정" -> AppContext.wiseSayingController.actionModify(rq);
                case "종료" -> status = AppContext.systemController.actionExit();
                case "빌드" -> AppContext.wiseSayingController.actionBuild();
                default -> System.out.println("알 수 없는 명령어입니다.");
            }
        }
        AppContext.sc.close();
    }
}
