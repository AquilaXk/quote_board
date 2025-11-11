package com.back;

import domain.system.controller.SystemController;
import domain.wiseSaying.controller.WiseSayingController;
import domain.wiseSaying.repository.WiseSayingRepository;
import domain.wiseSaying.service.WiseSayingService;

import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class AppContext {
    public static DateTimeFormatter forPrintDateTimeFormatter;
    public static WiseSayingRepository wiseSayingRepository;
    public static WiseSayingService wiseSayingService;
    public static WiseSayingController wiseSayingController;
    public static SystemController systemController;
    public static Scanner sc;


    public static void renew() {
        renew(new Scanner(System.in));
    }
    public static void renew(Scanner scanner) {
        sc = scanner;
        forPrintDateTimeFormatter = DateTimeFormatter.ofPattern("yy-MM-dd HH:mm:ss");
        wiseSayingRepository = new WiseSayingRepository("db/wiseSaying");
        wiseSayingService = new WiseSayingService(wiseSayingRepository);
        wiseSayingController = new WiseSayingController(wiseSayingService);
        systemController = new SystemController();
    }
}
