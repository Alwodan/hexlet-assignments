package exercise.connections;

import exercise.TcpConnection;

// BEGIN
public class Connected implements Connection {
    private final StringBuilder temp = new StringBuilder();
    private final TcpConnection connection;

    public Connected(TcpConnection connection) {
        this.connection = connection;
    }
    @Override
    public String getCurrentState() {
        return "connected";
    }

    @Override
    public void connect() {
        System.out.println("Error: trying to connect when already connected");
    }

    @Override
    public void disconnect() {
        System.out.println("Disconnected");
        connection.setState(new Disconnected(connection));
    }

    @Override
    public void write(String data) {
        temp.append(data);
    }

    public String getData() {
        return temp.toString();
    }
}
// END
