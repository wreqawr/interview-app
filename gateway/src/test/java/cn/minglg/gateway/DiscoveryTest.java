package cn.minglg.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

/**
 * ClassName:DiscoveryTest
 * Package:cn.minglg.gateway
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/12/10
 * @Version 1.0
 */

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class DiscoveryTest {
    @Autowired
    private DiscoveryClient discoveryClient;
//    private DiscoveryClient discoveryClient;

    @Test
    public void test1() {
        List<String> services = discoveryClient.getServices();
        for (String service : services) {
            System.out.println("==========" + service + "==========");
            discoveryClient.getInstances(service).forEach(item -> {
                System.out.println(item.getHost());
                System.out.println(item.getPort());
            });
        }
    }
}
