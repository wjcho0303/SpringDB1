package hello.springdb.service;

import hello.springdb.domain.Member;
import hello.springdb.repository.MemberRepositoryV1;
import lombok.RequiredArgsConstructor;

import java.sql.SQLException;

@RequiredArgsConstructor
public class MemberServiceV2 {

    private final MemberRepositoryV1 memberRepository;

    public void accountTransfer(String fromId, String toId, int money) throws SQLException {
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
