# How to use

```java
import net.flawe.rcon.Rcon;
import net.flawe.rcon.model.ConnectionResponse;

class Example {
    public static void main(String[] args) {
        Rcon rcon = new Rcon("127.0.0.1", 25575, "123");
        ConnectionResponse connectionResponse = rcon.connect();
        if (connectionResponse == ConnectionResponse.SUCCESS) {
            rcon.sendCommand("Hello, World!");
        }
    }
}
```

# Build

For build use this command:
``mvn clean package``

# How to start jar after build

``java -jar Rcon-1.0-SNAPSHOT.jar 127.0.0.1 25575 password``