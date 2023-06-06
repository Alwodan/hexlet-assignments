package exercise;
import exercise.connections.Connection;
import exercise.connections.Disconnected;

// BEGIN
public class TcpConnection {
    private Connection state;
    String ip;
    int port;

    public TcpConnection(String ip, int port) {
        setState(new Disconnected(this));
    }

    public void setState(Connection state) {
        this.state = state;
    }

    String getCurrentState() {
        return state.getCurrentState();
    }

    void connect() {
        state.connect();
    }
    void disconnect() {
        state.disconnect();
    }

    void write(String data) {
        state.write(data);
    }
}
// END
