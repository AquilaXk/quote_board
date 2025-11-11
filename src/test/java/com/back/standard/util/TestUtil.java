package com.back.standard.util;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class TestUtil {
    // 최초의 System.out 을 백업
    private static final PrintStream ORIGINAL_OUT = System.out;
    private static PrintStream CURRENT_OUT;

    // genScanner => GeneratorScanner
    public static Scanner genScanner(String input) {
        return new Scanner(input);
    }

    // 화면 출력 => 시스템 출력으로 변경
    public static ByteArrayOutputStream setOutToByteArray() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        CURRENT_OUT = new PrintStream(byteArrayOutputStream, true);
        System.setOut(CURRENT_OUT);

        return byteArrayOutputStream;
    }

    // 원상복귀 (시스템 출력 => 화면 출력)
    public static void clearSetOutToByteArray() {
        System.setOut(ORIGINAL_OUT);
        CURRENT_OUT.close();
        CURRENT_OUT = null;
    }
}
