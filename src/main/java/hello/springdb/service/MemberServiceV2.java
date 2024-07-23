package hello.springdb.service;

import hello.springdb.domain.Member;
import hello.springdb.repository.MemberRepositoryV2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 트랜잭션 - 파라미터 연동, Pool을 고려한 종료
 */
@RequiredArgsConstructor
@Slf4j
public class MemberServiceV2 {

    private final DataSource dataSource;
    private final MemberRepositoryV2 memberRepository;

    public void accountTransfer(String fromId, String toId, int money) throws SQLException {
        Connection conn = dataSource.getConnection();
        try {
            // 수동커밋 모드 설정 = 트랜잭션 시작
            conn.setAutoCommit(false);

            // 비즈니스 로직 수행: 파라미터에 Connection 전달
            accountTransferBizLogic(conn, toId, money, fromId);

            // 성공 시 커밋
            conn.commit();
        } catch (Exception e) {
            // 실패 시 롤백
            conn.rollback();
            throw new IllegalStateException(e);
        } finally {
            // Connection 릴리즈
            release(conn);
        }
    }

    private void accountTransferBizLogic(Connection conn, String toId, int money, String fromId) throws SQLException {
        Member fromMember = memberRepository.findById(conn, fromId);
        Member toMember = memberRepository.findById(conn, toId);

        memberRepository.update(conn, fromId, fromMember.getMoney() - money);
        validate(toMember); // 학습을 위한 의도적 예외 발생 코드
        memberRepository.update(conn, toId, toMember.getMoney() + money);
    }

    private static void release(Connection conn) {
        if (conn != null) {
            try {
                // 커넥션 풀을 고려하여, 자동커밋 모드로 다시 원복시킨 후 close 해야 한다.
                conn.setAutoCommit(true);
                conn.close();
            } catch (Exception e) {
                log.info("error", e);
            }
        }
    }

    // toMember의 Id를 "ex"로 해놓고 예외 터트리기
    private static void validate(Member toMember) {
        if (toMember.getMemberId().equals("ex")) {
            throw new IllegalStateException("이체 중 예외 발생");
        }
    }
}
