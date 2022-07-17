package org.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.*;

import java.time.Duration;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*; // jupiter는 junit 5
import static org.junit.jupiter.api.Assumptions.*;
import static org.assertj.core.api.Assertions.*;

/**
 * Unit test for simple App.
 */

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class) // 테스트 이름 지어주는 전략
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    @DisplayName("hello world 😀")
    public void create_new_study(){ // 전력에 따르면 공백 없애고 create new study로 나옴
        Study study = new Study();
        assertNotNull(study);
        //assertEquals(StudyStatus.DRAFT, study.getStatus(), "스터디를 처음 만들면 상태값이 DRAFT여야 한다.");
        assertEquals(StudyStatus.DRAFT, study.getStatus(), new Supplier<String>() {
            @Override
            public String get() {
                return "같은 표현 람다식으로도 가능, 함수형 인터페이스를 넘겨주면 미리 메모리를 안쓰기 때문에 성능 향상";
            }
        });
        assertTrue(study.getLimit() > 0, () -> " 스터디 최대 참석 가능 인원은 0보다 커야 한다.");

        // 앞에중 하나가 실패하면 뒤는 확인할 수 없다, assertAll로 묶어주면 한번에 확인 가능
        assertAll(
                () -> assertNotNull(study),
                () ->  assertTrue(study.getLimit() > 0, () -> " 스터디 최대 참석 가능 인원은 0보다 커야 한다."),
                () -> assertEquals(StudyStatus.DRAFT, study.getStatus(), new Supplier<String>() {
                    @Override
                    public String get() {
                        return "같은 표현 람다식으로도 가능, 함수형 인터페이스를 넘겨주면 미리 메모리를 안쓰기 때문에 성능 향상";
                    }
                })
        );
    }

    @Test
    @DisplayName("다른 Assertion 예제")
    public void assertion_practice(){
        Study study = Study.builder()
                        .limit(-10)
                        .build();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> study.testLimit());
        String message = ex.getMessage();
        assertEquals("limit은 0보다 커야한다.", ex.getMessage());

        assertTimeout(Duration.ofMillis(10), () -> {
            new Study();
            Thread.sleep(100);
            // 함수 바디 만큼 시간 걸림
        });

        assertTimeoutPreemptively(Duration.ofMillis(10), () -> {
            new Study();
            Thread.sleep(100);
            // 조건 만큼 ( 위의 10 밀리세컨드만 확인하고 테스트 결과가 나타남 )
        });

        assertThat(new Study(StudyStatus.DRAFT, 10).getLimit()).isGreaterThan(0);
    }

    @Test
    @DisplayName("조건에 따라 테스트 실행하기")
    @EnabledOnOs({OS.LINUX, OS.MAC})
    @EnabledOnJre({JRE.JAVA_8, JRE.JAVA_11})
    @EnabledIfEnvironmentVariable(named = "TEST_ENV", matches = "LOCAL")
    // @DisabledOnJre({JRE.JAVA_9}) Disabled도 있음
    public void condition_test(){
        assumeTrue("LOCAL".equalsIgnoreCase(System.getenv("TEST_ENV")));

        assumingThat("LOCAL".equalsIgnoreCase(System.getenv("TEST_ENV")), () -> {
            assertThat(new Study(StudyStatus.DRAFT, 10).getLimit()).isGreaterThan(0);
        });
    }

}
