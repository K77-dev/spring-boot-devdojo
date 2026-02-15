package academy.devdojo.config;

import external.dependency.Connection;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ConnectionConfiguration {

    @Bean
    //@Bean(name = "connection")
    @Primary
    public Connection connectionMySql(){
        return new Connection("localhost", "devdojoMySql","goku");
    }

    @Bean
    //@Bean(name = "connectionMongoDB")
    public Connection connectionMongo(){
        return new Connection("localhost", "devdojoMongo","goku");
    }

}
