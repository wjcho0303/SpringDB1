package hello.springdb.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@Slf4j
class CheckedTest {

    @Test
    void checked_catch() {
        Service service = new Service();
        service.catchMethod();
    }

    @Test
    void checked_throw() {
        Service service = new Service();
        assertThatThrownBy(() -> service.throwMethod())
                .isInstanceOf(MyCheckedException.class);
    }

    /**
     * Exception을 상속받은 예외는 체크 예외가 되어 컴파일러가 체크해준다.
     */
    static class MyCheckedException extends Exception {
        public MyCheckedException(String message) {
            super(message);
        }
    }

    /**
     * Checked 예외는 catch 하여 처리하거나, throw + throws로 던지거나 둘 중 하나를 필수로 해야 한다.
     */
    static class Service {
        Repository repository = new Repository();

        public void catchMethod() {
            try {
                repository.callRepository();
            } catch (MyCheckedException e) {
                log.info("예외 처리, message = {}", e.getMessage(), e);
            }
        }

        public void throwMethod() throws MyCheckedException {
            repository.callRepository();
        }
    }

    static class Repository {
        public void callRepository() throws MyCheckedException {
            throw new MyCheckedException("ex");
        }
    }
}
