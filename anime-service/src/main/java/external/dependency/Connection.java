package external.dependency;

import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class Connection {
    private String host;
    private String username;
    private String password;
}
