package com.example.k8s.handler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;

@SpringBootApplication
public class Application {
	
	@Value("${k8s.api.url}")
	private String _KUBEURL;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	@Bean
	public KubernetesClient kubernetesClient() {
		Config config = new ConfigBuilder().withMasterUrl(_KUBEURL).build();
		KubernetesClient kube = new DefaultKubernetesClient(config);
		return kube;
	}
}
