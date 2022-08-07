package org.example.study;

import lombok.RequiredArgsConstructor;
import org.example.domain.Member;
import org.example.domain.Study;
import org.example.member.MemberService;

import java.util.Optional;

@RequiredArgsConstructor // final 필드 생성자 생성
public class StudyService {

    private final MemberService memberService;

    private final StudyRepository studyRepository;

    public Study createNewStudy(Long memberId, Study study){
        Optional<Member> member = memberService.findById(memberId);
        study.setOwner(member.orElseThrow(() -> new IllegalArgumentException("Member doesn't exist for id: " + memberId)));
        return studyRepository.save(study);
    }
}
