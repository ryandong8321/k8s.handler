package com.example.k8s.handler.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.fabric8.kubernetes.api.KubernetesHelper;
import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.ServiceList;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;

public class K8sHandlerService {
	protected final Logger logger = LoggerFactory.getLogger(K8sHandlerService.class);
	protected final String _KUBEURL="http://100.98.42.254:8001";
	protected final String _DEFAULT_NAMESPACE="default";
	
	public void updateService(String serviceName) throws Exception {
		Config config = new ConfigBuilder().withMasterUrl(_KUBEURL).build();
		KubernetesClient kube = new DefaultKubernetesClient(config);
		
		ServiceList sevList=kube.services().inNamespace(_DEFAULT_NAMESPACE).list();
		if (sevList!=null) {
			for (Service service:sevList.getItems()) {
				logger.debug("current Service name {}", service.getMetadata().getName());
				if (service!=null&&KubernetesHelper.getName(service).equals(serviceName)) {
					kube.services().inNamespace(_DEFAULT_NAMESPACE).withLabel("app", service.getMetadata().getLabels().get("app")).delete();
					kube.services().inNamespace(_DEFAULT_NAMESPACE).createNew()
					.withApiVersion(service.getApiVersion())
					.withNewMetadata().withName(serviceName).addToLabels("app", serviceName).endMetadata()
					.withSpec(service.getSpec()).done();
//					kube.services().inNamespace(_DEFAULT_NAMESPACE).withLabel("app", service.getMetadata().getLabels().get("app")).delete();	
//					Service result=kube.services().inNamespace(_DEFAULT_NAMESPACE).createOrReplace(service);
//					logger.debug("update Service success {}", result.getMetadata().getName());
					break;
				}
			}
		}
		
	}
	
	public void createDeployment(String filePath) throws FileNotFoundException {
		Config config = new ConfigBuilder().withMasterUrl(_KUBEURL).build();
		KubernetesClient kube = new DefaultKubernetesClient(config);
		
		File file=new File("C:\\uploadfiles\\apollo.yml");
		
		List<HasMetadata> resources = kube.load(new FileInputStream(file)).get();
		if (resources.isEmpty()) {
			logger.debug("yaml file is empty");
			return;
		}
		
		for (HasMetadata resource:resources) {
			if (resource instanceof Deployment) {
				Deployment deployment=(Deployment)resource;
				Deployment result=kube.apps().deployments().inNamespace(_DEFAULT_NAMESPACE).createOrReplace(deployment);
				logger.debug("create deployment success {}", result.getMetadata().getName());
			}else if (resource instanceof Service) {
				Service service=(Service)resource;
				Service result=null;
				kube.services().inNamespace(_DEFAULT_NAMESPACE).withLabel("app", service.getMetadata().getLabels().get("app")).delete();
				result=kube.services().inNamespace(_DEFAULT_NAMESPACE).createOrReplace(service);
				logger.debug("create Service success {}", result.getMetadata().getName());
			}
		}
	}
	
	
	public static void main(String[] args) {
		try {
//			FileInputStream fis=new FileInputStream(file);
//			Deployment impl=KubernetesHelper.loadYaml(file);
//			Service service=KubernetesHelper.loadYaml(file, Service.class);
//			service=kube.services().inNamespace(_DEFAULT_NAMESPACE).createOrReplace(service);
//			Service service=KubernetesHelper.loadYaml(new File("C:\\uploadfiles\\apollo-service.yml"));
//			Deployment impl = kube.apps().deployments().inNamespace(_DEFAULT_NAMESPACE).load(file).createOrReplaceWithNew().done();
//			impl=kube.apps().deployments().inNamespace(_DEFAULT_NAMESPACE).createOrReplace(impl);
//			logger.info("impl name: ", KubernetesHelper.getName(impl));
//			logger.info("service name: ", KubernetesHelper.getName(service));
//			Pod pod=kube.pods().inNamespace("default").load(file).createOrReplaceWithNew().done();
//			logger.info("Pod name: ", KubernetesHelper.getName(pod));
//			Controller controller=new Controller(kube);
//			controller.setNamespace(_DEFAULT_NAMESPACE);
//			controller.applyYaml(file);
			
			
//			ServiceList serviceList=kube.services().inNamespace(_DEFAULT_NAMESPACE).list();
//			if (serviceList!=null) {
//				String serviceName=null;
//				for (Service service:serviceList.getItems()) {
//					serviceName=KubernetesHelper.getName(service);
//					logger.debug("service name: {}", KubernetesHelper.getName(service));
//					if (serviceName!=null&&!serviceName.equals("")&&serviceName.equals("apollo")) {
//						logger.debug("service name: {} updated", serviceName);
//						
//						kube.services().inNamespace(_DEFAULT_NAMESPACE).createOrReplace(service);
//						
////						Controller ctl=new Controller();
////						ctl.setNamespace(_DEFAULT_NAMESPACE);
////						ctl.applyService(service, serviceName);
////						logger.debug("service name: {} create", KubernetesHelper.getName(service));
//					}
//				}
//			}
			
			K8sHandlerService service=new K8sHandlerService();
//			service.createDeployment(null);
			
			service.updateService("apollo");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
