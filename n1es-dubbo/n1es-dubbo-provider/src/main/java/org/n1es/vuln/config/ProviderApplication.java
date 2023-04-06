package org.n1es.vuln.config;

import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@DubboComponentScan("org.n1es.vuln.*")
public class ProviderApplication {

	public static void main(String[] args) {
		// 启动嵌入式ZooKeeper
		new EmbeddedZooKeeper(2181, false).start();

		// 启动Dubbo服务
		SpringApplication.run(ProviderApplication.class, args);
	}

}
