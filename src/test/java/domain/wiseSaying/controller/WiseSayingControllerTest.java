package domain.wiseSaying.controller;

import com.back.AppTestRunner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

public class WiseSayingControllerTest {
    @Test
    @DisplayName("등록")
    void t1() {
        final String out = AppTestRunner.run("""
                등록
                현재를 사랑하라.
                작자미상
                종료
                """);

        assertThat(out)
                .contains("명언 :")
                .contains("작가 :")
                .contains("1번 명언이 등록되었습니다.");
    }

    @Test
    @DisplayName("등록 후 목록확인")
    void t2() {
        final String out = AppTestRunner.run("""
                등록
                나의 죽음을 적들에게 알리지 말라!
                이순신
                목록
                종료
                """);

        assertThat(out)
                .contains("번호 / 작가 / 명언")
                .contains("1 / 이순신 / 나의 죽음을 적들에게 알리지 말라!")
                .contains("1번 명언이 등록되었습니다.");
    }

    @Test
    @DisplayName("2개 등록 후 목록확인")
    void t3() {
        final String out = AppTestRunner.run("""
                등록
                나의 죽음을 적들에게 알리지 말라!
                이순신
                등록
                나는 내가 아무것도 모른다는 것을 안다.
                소크라테스
                목록
                종료
                """);

        assertThat(out)
                .contains("2 / 소크라테스 / 나는 내가 아무것도 모른다는 것을 안다.")
                .contains("1 / 이순신 / 나의 죽음을 적들에게 알리지 말라!");
    }

    @Test
    @DisplayName("삭제")
    void t4() {
        final String out = AppTestRunner.run("""
                등록
                나의 죽음을 적들에게 알리지 말라!
                이순신
                등록
                나는 내가 아무것도 모른다는 것을 안다.
                소크라테스
                삭제?id=1
                목록
                종료
                """);

        assertThat(out)
                .contains("1번 명언이 삭제되었습니다.")
                .contains("2 / 소크라테스 / 나는 내가 아무것도 모른다는 것을 안다.")
                .doesNotContain("1 / 이순신 / 나의 죽음을 적들에게 알리지 말라!");
    }

    @Test
    @DisplayName("존재하지 않는 명언 삭제")
    void t5() {
        final String out = AppTestRunner.run("""
                등록
                나의 죽음을 적들에게 알리지 말라!
                이순신
                삭제?id=10
                종료
                """);

        assertThat(out)
                .contains("10번 명언은 존재하지 않습니다.");
    }

    @Test
    @DisplayName("수정")
    void t6() {
        final String out = AppTestRunner.run("""
                등록
                나의 죽음을 적들에게 알리지 말라!
                이순신
                수정?id=1
                나의 죽음을 적들에게 알려라!
                충무공 이순신
                목록
                종료
                """);

        assertThat(out)
                .contains("명언(기존) 나의 죽음을 적들에게 알리지 말라!")
                .contains("작가(기존) 이순신")
                .contains("1 / 충무공 이순신 / 나의 죽음을 적들에게 알려라!");
    }

    @Test
    @DisplayName("없는 명언 수정 시도")
    void t7() {
        final String out = AppTestRunner.run("""
                등록
                나의 죽음을 적들에게 알리지 말라!
                이순신
                수정?id=10
                종료
                """);

        assertThat(out)
                .contains("10번 명언은 존재하지 않습니다.");
    }

    @Test
    @DisplayName("빌드")
    void t8() {
        AppTestRunner.run("""
                등록
                나의 죽음을 적들에게 알리지 말라!
                이순신
                종료
                """);

        final String out = AppTestRunner.run("""
                빌드
                종료
                """);

        assertThat(out).isNotNull();
    }
}
