package hello.springdb.connection;

public abstract class ConnectionConstant {  // 상수만 다루므로 객체를 생성하면 안 되기 때문에 abstract
    private static final String URL = "jdbc:h2:tcp://localhost/~/test";
    private static final String USERNAME = "sa";
    private static final String PASSWORD = "";
}
