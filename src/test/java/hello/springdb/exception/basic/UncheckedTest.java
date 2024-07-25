package hello.springdb.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@Slf4j
public class UncheckedTest {

    @Test
    void unchecked_catch() {
        Service service = new Service();
        service.catchMethod();
    }

    @Test
    void unchecked_throw() {
        Service service = new Service();
        assertThatThrownBy(() -> service.throwMethod())
                .isInstanceOf(MyUncheckedException.class);
    }


    /**
     * RuntimeException을 상속받은 예외는 언체크 예외가 된다.
     */
    static class MyUncheckedException extends RuntimeException {
        public MyUncheckedException(String message) {
            super(message);
        }
    }

    static class Repository {
        public void callRepository() {
            throw new MyUncheckedException("ex");
        }
    }

    /**
     * 언체크 예외는 예외를 잡거나 던지지 않아도 된다. 만약 예외를 잡지 않으면 자동으로 던진다.
     */
    static class Service {
        Repository repository = new Repository();

        public void catchMethod() {
            try {
                repository.callRepository();
            } catch (MyUncheckedException e) {
                log.info("예외 처리, message = {}", e.getMessage(), e);
            }
        }

        /**
         * 예외를 잡지 않아도 자연스럽게 상위로 던져짐
         */
        public void throwMethod() {
            repository.callRepository();
        }
    }
}
