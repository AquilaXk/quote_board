package com.back;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;



import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class AppTest {
    @Test
    @DisplayName("== 명언 앱 ==")
    void t1() {
        String rs = AppTestRunner.run("종료");

        assertThat(rs)
                .contains("== 명언 앱 ==")
                .contains("프로그램이 종료되었습니다.");
    }

}
