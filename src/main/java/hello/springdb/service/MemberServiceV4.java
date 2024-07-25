package hello.springdb.service;

import hello.springdb.domain.Member;
import hello.springdb.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

/**
 * 예외 누수 문제 해결
 * SQLException 제거
 * MemberRepository 인터페이스에 의존
 */
@Slf4j
@Transactional
public class MemberServiceV4 {

    private final MemberRepository memberRepository;

    public MemberServiceV4(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void accountTransfer(String fromId, String toId, int money) {
        accountTransferBizLogic(toId, money, fromId);
    }

    private void accountTransferBizLogic(String toId, int money, String fromId) {
        Member fromMember = memberRepository.findById(fromId);
        Member toMember = memberRepository.findById(toId);

        memberRepository.update(fromId, fromMember.getMoney() - money);
        validate(toMember); // 학습을 위한 의도적 예외 발생 코드
        memberRepository.update(toId, toMember.getMoney() + money);
    }

    // toMember의 Id를 "ex"로 해놓고 예외 터트리기
    private static void validate(Member toMember) {
        if (toMember.getMemberId().equals("ex")) {
            throw new IllegalStateException("이체 중 예외 발생");
        }
    }
}
