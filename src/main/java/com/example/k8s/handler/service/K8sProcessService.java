package com.example.k8s.handler.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import io.fabric8.kubernetes.api.KubernetesHelper;
import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.ServiceList;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.client.KubernetesClient;

@Component
public class K8sProcessService {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	protected final String _DEFAULT_NAMESPACE="default";

	@Autowired
	private KubernetesClient kube;
	
	public void updateService(String serviceName) throws Exception {
		ServiceList sevList=kube.services().inNamespace(_DEFAULT_NAMESPACE).list();
		if (sevList!=null) {
			for (Service service:sevList.getItems()) {
				logger.info("current Service name {}", service.getMetadata().getName());
				if (service!=null&&KubernetesHelper.getName(service).equals(serviceName)) {
					//delete service
					kube.services().inNamespace(_DEFAULT_NAMESPACE).withLabel("app", service.getMetadata().getLabels().get("app")).delete();
					
					//create service
					Service result=kube.services().inNamespace(_DEFAULT_NAMESPACE).createNew()
					.withApiVersion(service.getApiVersion())
					.withNewMetadata().withName(serviceName).addToLabels("app", serviceName).endMetadata()
					.withSpec(service.getSpec()).done();
					logger.info("update Service success {}", result.getMetadata().getName());
					break;
				}
			}
		}
	}
	
	public void createDeployment(String filePath) throws FileNotFoundException {
		
		File file=new File("C:\\uploadfiles\\apollo.yml");
		
		List<HasMetadata> resources = kube.load(new FileInputStream(file)).get();
		if (resources.isEmpty()) {
			logger.info("yaml file is empty");
			return;
		}
		
		for (HasMetadata resource:resources) {
			if (resource instanceof Deployment) {
				Deployment deployment=(Deployment)resource;
				Deployment result=kube.apps().deployments().inNamespace(_DEFAULT_NAMESPACE).createOrReplace(deployment);
				logger.info("create deployment success {}", result.getMetadata().getName());
			}else if (resource instanceof Service) {
				Service service=(Service)resource;
				Service result=null;
				kube.services().inNamespace(_DEFAULT_NAMESPACE).withLabel("app", service.getMetadata().getLabels().get("app")).delete();
				result=kube.services().inNamespace(_DEFAULT_NAMESPACE).createOrReplace(service);
				logger.info("create Service success {}", result.getMetadata().getName());
			}
		}
	}
	
}
