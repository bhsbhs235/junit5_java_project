package org.example;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregationException;
import org.junit.jupiter.params.aggregator.ArgumentsAggregator;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.SimpleArgumentConverter;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Duration;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*; // jupiter는 junit 5
import static org.junit.jupiter.api.Assumptions.*;
import static org.assertj.core.api.Assertions.*;

/**
 * Unit test for simple App.
 */

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class) // 테스트 이름 지어주는 전략
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // 테스트간 의존성이 필요한 경우에 사요
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // 테스트 순서를 지정 없다고 해서
public class AppTest
{
    /**
     * Rigorous Test :-)
     */

    @BeforeAll // 통합 테스트 전에 실행
    static void beforeAll(){
        System.out.println("before all");
    }

    @AfterAll
    static void afterAll(){
        System.out.println("after all");
    }

    @BeforeEach // 각 테스트 단위로 실행하기 전에 실행
    public void beforeEach(){
        System.out.println("before each");
    }

    @AfterEach
    public void afterEach(){
        System.out.println("after each");
    }

    @Disabled
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

    @Disabled
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

        assertThat(new Study(StudyStatus.DRAFT, 10, "자바").getLimit()).isGreaterThan(0);
    }

    @Disabled
    @Test
    @DisplayName("조건에 따라 테스트 실행하기")
    @EnabledOnOs({OS.LINUX, OS.MAC})
    @EnabledOnJre({JRE.JAVA_8, JRE.JAVA_11})
    @EnabledIfEnvironmentVariable(named = "TEST_ENV", matches = "LOCAL")
    // @DisabledOnJre({JRE.JAVA_9}) Disabled도 있음
    public void condition_test(){
        assumeTrue("LOCAL".equalsIgnoreCase(System.getenv("TEST_ENV")));

        assumingThat("LOCAL".equalsIgnoreCase(System.getenv("TEST_ENV")), () -> {
            assertThat(new Study(StudyStatus.DRAFT, 10, "자바").getLimit()).isGreaterThan(0);
        });
    }

    @Test
    @DisplayName("태깅과 필터링")
    @Tag("fast") // 태깅을 하면 태깅 옵션("fast")한것만 테스트 진행할 수 있다(intellij에서).
    public void tagging_test(){
        Study study = new Study();
        assertNotNull(study);
    }

    @FastTest
    @DisplayName("커스텀 태그")
    public void custom_tag_test(){
        Study study = new Study();
        assertNotNull(study);
    }

    @DisplayName("테스트 반복하기")
    @RepeatedTest(value = 3, name = "{displayName}, {currentRepetition}/{totalRepetitions}")
    public void repeat_test(RepetitionInfo repetitionInfo){
        System.out.println("test" + repetitionInfo.getCurrentRepetition() + "/" + repetitionInfo.getTotalRepetitions());
    }

    @DisplayName("테스트 반복하기2")
    @ParameterizedTest(name = "{index} {displayName} message={0}")
    @ValueSource(strings = {"날씨가" , "너무" , "떱다"})
    @EmptySource // string 배열에 빈값 추가
    @NullSource // string 배열에 null값 추가
    public void repeat_test_param(String message) {
        System.out.println(message);
    }

    @DisplayName("테스트 반복하기3")
    @ParameterizedTest(name = "{index} {displayName} message={0}")
    @CsvSource({"10, '자바'", "20, '스프링'"})
    public void repeat_test_param2(ArgumentsAccessor argumentsAccessor) {
        Study study = Study.builder()
                .limit(argumentsAccessor.getInteger(0))
                .name(argumentsAccessor.getString(1))
                .build();
    }

    @DisplayName("테스트 반복하기4")
    @ParameterizedTest(name = "{index} {displayName} message={0}")
    @CsvSource({"10, '자바'", "20, '스프링'"})
    public void repeat_test_param3(@AggregateWith(StudyAggregator.class) Study study) {
        System.out.println(study.getLimit() + " / " + study.getName());
    }

    int value = 1;

    @Test
    @Order(2)
    public void instance_test(){
        System.out.println("value " + value++);
    }

    @Test
    @Order(1) // 테스트 순서를 지정해줌
    // instance_test value 값은 동일하다 테스트 간의 의존성을 없애기 위해 새로운 인스턴스로 테스트 하기 때문에
    // @TestInstance(TestInstance.Lifecycle.PER_CLASS) 를 해주면 인스턴스가 동일해서 value 값이 변경된 만큼 각각 적용된다
    // @BeforeAll, @BeforeEach 도 static을 때줘도 된다.(인스턴스가 동일하기 때문에)
    public void instance_test2(){
        System.out.println("value " + value++);
    }

    static class StudyAggregator implements ArgumentsAggregator {

        @Override
        public Object aggregateArguments(ArgumentsAccessor argumentsAccessor, ParameterContext parameterContext) throws ArgumentsAggregationException {
            return Study.builder()
                    .limit(argumentsAccessor.getInteger(0))
                    .name(argumentsAccessor.getString(1))
                    .build();
        }
    }
}
