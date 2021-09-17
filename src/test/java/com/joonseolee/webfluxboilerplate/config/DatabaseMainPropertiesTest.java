package com.joonseolee.webfluxboilerplate.config;

import com.joonseolee.webfluxboilerplate.config.database.DatabaseMainProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@ActiveProfiles("default")
@SpringBootTest(properties = "classpath:application.yml")
class DatabaseMainPropertiesTest {

    @Autowired
    private DatabaseMainProperties databaseMainProperties;

    @Test
    void whenLoadDatabaseMainProperties_thenReturnObjectMappedValues() {
        var master = databaseMainProperties.getMaster();
        var slaves = databaseMainProperties.getSlaves();

        assertThat(master.getUrl(), is("jdbc:mysql://localhost:3306/master?serverTimezone=Asia/Seoul"));
        assertThat(slaves.get(0).getUrl(), is("jdbc:mysql://localhost:3306/slave?serverTimezone=Asia/Seoul"));
    }
}
