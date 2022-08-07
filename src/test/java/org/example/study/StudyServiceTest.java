package org.example.study;

import org.example.domain.Member;
import org.example.domain.Study;
import org.example.member.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // 추가해줘야 @Mock 애노테이션이 적용된다.
public class StudyServiceTest {

    @Mock
    MemberService memberService;

    @Mock
    StudyRepository studyRepository;

    @Test
    @DisplayName("Mock객체 생성")
    void createStudyService(){
        // 구현체는 없지만 의존 하고 있는 인터페이스를 써야 할 때 모킹을 해줘야한다.
        // 내가 테스트 하고 싶은건 StudyService 인데 필요한 MemberService와 StudyRepository가 준비되어 있지 않거나 또는 있더라도 그 구현체를 사용하지 않고,
        // StudyService를 테스트 하고 싶을때 Mock객체를 만들어서 주입해준다.

        // MemberService memberService = mock(MemberService.class);
        // StudyRepository studyRepository = mock(StudyRepository.class);

        Optional<Member> optional = memberService.findById(1L);
        assertTrue(optional.isEmpty());

        memberService.validate(2L); // 예외가 발생하지 않고, 아무일도 일어나지 않고 지나감

        StudyService studyService = new StudyService(memberService, studyRepository);
        assertNotNull(studyService);
    }

    @Test
    @DisplayName("Mock Stubbing")
    void mockStubbing(){
        StudyService studyService = new StudyService(memberService, studyRepository);
        assertNotNull(studyService);

        Member member = new Member();
        member.setId(1L);
        member.setEmail("bhsbhs235@github.com");

        //stubbing이란, Mock객체의 행동을 조작하는 것을 말한다.
        //when(memberService.findById(1L)).thenReturn(Optional.of(member));
        //when(memberService.findById(2L)).thenThrow(new IllegalArgumentException("에러 테스트"));
        //doThrow(new IllegalArgumentException()).when(memberService).validate(1L);
        when(memberService.findById(any()))
                .thenReturn(Optional.of(member)) // 첫번째 리턴
                .thenThrow(new RuntimeException()) // 두번째 리턴
                .thenReturn(Optional.empty()); // 세번째 리턴

        Optional<Member> resultMember = memberService.findById(1L);
        assertEquals("bhsbhs235@github.com", resultMember.get().getEmail());

        assertThrows(RuntimeException.class, () -> {
            memberService.findById(2L);
        });

        assertTrue(memberService.findById(3L).isEmpty());
    }

    @Test
    @DisplayName("Mock Stubbing exception")
    void mockStubbingException(){
        StudyService studyService = new StudyService(memberService, studyRepository);
        assertNotNull(studyService);

        Member member = new Member();
        member.setId(1L);
        member.setEmail("bhsbhs235@github.com");

        doThrow(new IllegalArgumentException()).when(memberService).validate(1L);

        assertThrows(IllegalArgumentException.class, ()->{
            memberService.validate(1L);
        });
    }

    @Test
    @DisplayName("Mock Stubbing 연습문제")
    void example1(){
        StudyService studyService = new StudyService(memberService, studyRepository);
        assertNotNull(studyService);

        Study study = new Study(10, "테스트");

        Member member = new Member();
        member.setId(1L);
        member.setEmail("bhsbhs235@github.com");

        when(memberService.findById(1L))
                .thenReturn(Optional.of(member));

        when(studyRepository.save(study))
                .thenReturn(study);

        studyService.createNewStudy(1L, study);
        assertEquals(member, study.getOwner());
    }
}