package hello.springdb.repository;

import hello.springdb.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import static hello.springdb.connection.ConnectionConstant.*;
import static org.assertj.core.api.Assertions.*;

@Slf4j
class MemberRepositoryV1Test {

    MemberRepositoryV1 memberRepository;

    @BeforeEach
    void beforeEach() {
        // 기본 DriverManager - 항상 새로운 커넥션 획득
        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
        memberRepository = new MemberRepositoryV1(dataSource);
    }

    @Test
    void crud() throws SQLException {
        // save
        Member member = new Member("memberV0", 10000);
        memberRepository.save(member);

        // findById
        Member findMember = memberRepository.findById(member.getMemberId());
        log.info("findMember = {}", findMember);
        assertThat(findMember).isEqualTo(member);

        // update: money를 20000으로 수정하는 요청
        memberRepository.update(member.getMemberId(), 20000);
        Member updatedMember = memberRepository.findById(member.getMemberId());
        log.info("updatedMember: {}", updatedMember);
        assertThat(updatedMember.getMoney()).isEqualTo(20000);

        // delete
        memberRepository.delete(member.getMemberId());
        assertThatThrownBy(() -> memberRepository.findById(member.getMemberId()))
                .isInstanceOf(NoSuchElementException.class);
    }
}