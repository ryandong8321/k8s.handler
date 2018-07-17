package com.example.k8s.handler.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.fabric8.kubernetes.api.KubernetesHelper;
import io.fabric8.kubernetes.api.model.PodSpec;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.Resource;

public class K8sHandlerService {
	
	
	public static void main(String[] args) {
		String _KUBEURL="http://100.98.42.254:8001";
		String _DEFAULT_NAMESPACE="default";
		
		Logger logger = LoggerFactory.getLogger(K8sHandlerService.class);
		
		Config config = new ConfigBuilder().withMasterUrl(_KUBEURL).build();
		KubernetesClient kube = new DefaultKubernetesClient(config);
		
//		JSONArray arr=new JSONArray();
//		ServiceList services = kube.services().list();
//		List<Service> serviceItems = services.getItems();
//		Map<String, String> map=null;
//		for (Service service : serviceItems) {
//			map=new HashMap<>();
//			logger.info("Service [{}], labels: [{}], selector: [{}], ports: [{}]", new Object[] {KubernetesHelper.getName(service), service.getMetadata().getLabels(), getSelector(service), getPorts(service)});
//			map.put("Service", KubernetesHelper.getName(service));
//			map.put("labels", service.getMetadata().getLabels()==null?"":service.getMetadata().getLabels().toString());
//			map.put("selector", getSelector(service)==null?"":getSelector(service).toString());
//			map.put("ports", getPorts(service)==null?"":getPorts(service).toString());
//			arr.put(map);
//		}
		
		File file=new File("C:\\uploadfiles\\apollo.yml");
		
		try {
			FileInputStream fis=new FileInputStream(file);
			Deployment impl=KubernetesHelper.loadYaml(file);
//			Service service=KubernetesHelper.loadYaml(file, Service.class);
//			service=kube.services().inNamespace(_DEFAULT_NAMESPACE).createOrReplace(service);
//			Service service=KubernetesHelper.loadYaml(new File("C:\\uploadfiles\\apollo-service.yml"));
//			Deployment impl = kube.apps().deployments().inNamespace(_DEFAULT_NAMESPACE).load(file).createOrReplaceWithNew().done();
			impl=kube.apps().deployments().inNamespace(_DEFAULT_NAMESPACE).createOrReplace(impl);
			logger.info("impl name: ", KubernetesHelper.getName(impl));
//			logger.info("service name: ", KubernetesHelper.getName(service));
//			Pod pod=kube.pods().inNamespace("default").load(file).createOrReplaceWithNew().done();
//			logger.info("Pod name: ", KubernetesHelper.getName(pod));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
