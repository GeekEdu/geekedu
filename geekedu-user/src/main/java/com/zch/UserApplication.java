package com.zch;

import com.zch.common.core.listener.AppStartupListener;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

/**
 * @author Poison02
 * @date 2024/1/6
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@MapperScan("com.zch.user.mapper")
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }

    @Bean("userAppStartupListener")
    public AppStartupListener appStartupListener(){
        return new AppStartupListener();
    }

//    @Bean
//    CommandLineRunner init(ConfigurableEnvironment environment) {
//        return args -> {
//            String hostAddress = findHostAddress();
//            if (hostAddress != null) {
//                System.out.println("Detected host IP: " + hostAddress);
//
//                MutablePropertySources propertySources = environment.getPropertySources();
//                Map<String, Object> myMap = new HashMap<>();
//                myMap.put("xxl.job.executor.ip", hostAddress);
//                myMap.put("xxl.job.executor.address", "http://" + hostAddress + ":9089");
//                PropertySource<?> dynamicPropertySource = new MapPropertySource("dynamicProperties", myMap);
//                propertySources.addFirst(dynamicPropertySource);
//                System.out.println("-==================" + myMap);
//                System.out.println("-==============" + propertySources);
//            }
//        };
//    }
//
//    private String findHostAddress() throws Exception {
//        Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
//        while (netInterfaces.hasMoreElements()) {
//            NetworkInterface ni = netInterfaces.nextElement();
//            Enumeration<InetAddress> addresses = ni.getInetAddresses();
//            while (addresses.hasMoreElements()) {
//                InetAddress addr = addresses.nextElement();
//                if (!addr.isLoopbackAddress() && !addr.isLinkLocalAddress() && addr.isSiteLocalAddress()) {
//                    return addr.getHostAddress();
//                }
//            }
//        }
//        return null;
//    }

}
