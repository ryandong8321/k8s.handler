package com.example.k8s.handler.web;

import static io.fabric8.kubernetes.api.KubernetesHelper.getPorts;
import static io.fabric8.kubernetes.api.KubernetesHelper.getSelector;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.fabric8.kubernetes.api.KubernetesHelper;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.DoneableService;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodBuilder;
import io.fabric8.kubernetes.api.model.PodList;
import io.fabric8.kubernetes.api.model.PodSpec;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.ServiceList;
import io.fabric8.kubernetes.client.KubernetesClient;

@RestController
public class K8sHandlerController {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private KubernetesClient kube;

	@GetMapping("/allpods")
	public String getAllPods() {
		logger.info("show all pods in kubernetes....");
		PodList lst = kube.pods().list();
		Map<String, String> map=null;
		JSONArray arr=new JSONArray();
		for (Pod item : lst.getItems()) {
			map=new HashMap<>();
			map.put("Pods", KubernetesHelper.getName(item));
			map.put("PodsIp", item.getStatus().getPodIP());
			map.put("createdAt", item.getMetadata().getCreationTimestamp());
			
			logger.info("Pods {} with ip: {} created: {}", new Object[] { KubernetesHelper.getName(item),
					item.getStatus().getPodIP(), item.getMetadata().getCreationTimestamp() });

			PodSpec podSpec = item.getSpec();
			if (podSpec != null) {
				List<Container> containers = podSpec.getContainers();
				if (containers != null) {
					for (Container container : containers) {
						logger.info("Container {}--{} ports: {}",
								new Object[] { container.getImage(), container.getCommand(), container.getPorts() });
						map.put("Container", container.getImage()+"--"+container.getCommand());
						map.put("ports", container.getPorts().toString());
					}
				}
			}
			arr.put(map);
		}
		return arr.toString();
	}

	@GetMapping("/allservices")
	public String getAllServices() {
		JSONArray arr=new JSONArray();
		ServiceList services = kube.services().list();
		List<Service> serviceItems = services.getItems();
		Map<String, String> map=null;
		for (Service service : serviceItems) {
			map=new HashMap<>();
			logger.info("Service [{}], labels: [{}], selector: [{}], ports: [{}]", new Object[] {KubernetesHelper.getName(service), service.getMetadata().getLabels(), getSelector(service), getPorts(service)});
			map.put("Service", KubernetesHelper.getName(service));
			map.put("labels", service.getMetadata().getLabels()==null?"":service.getMetadata().getLabels().toString());
			map.put("selector", getSelector(service)==null?"":getSelector(service).toString());
			map.put("ports", getPorts(service)==null?"":getPorts(service).toString());
			arr.put(map);
		}
		return arr.toString();
	}
	
	@RequestMapping(value="/servicelist")
	@ResponseBody
	public String getServicesList(@RequestParam(name="namespace") String nameSpace, @RequestParam(name="servicename") String serviceName) {
		JSONArray arr=new JSONArray();
		Map<String, String> map=null;
		if (nameSpace==null||nameSpace.equals("")) {
			nameSpace="default";
		}
		if (serviceName!=null&&!serviceName.equals("")) {
			map=new HashMap<>();
			Service service = kube.services().inNamespace(nameSpace).withName(serviceName).get();
			map.put("Service", KubernetesHelper.getName(service));
			map.put("labels", service.getMetadata().getLabels()==null?"":service.getMetadata().getLabels().toString());
			map.put("selector", getSelector(service)==null?"":getSelector(service).toString());
			map.put("ports", getPorts(service)==null?"":getPorts(service).toString());
			arr.put(map);
		}else {
			ServiceList serviceList = kube.services().inNamespace("default").list();
			if (serviceList!=null) {
				for (Service service : serviceList.getItems()) {
					map=new HashMap<>();
					logger.info("Service [{}], labels: [{}], selector: [{}], ports: [{}]", new Object[] {KubernetesHelper.getName(service), service.getMetadata().getLabels(), getSelector(service), getPorts(service)});
					map.put("Service", KubernetesHelper.getName(service));
					map.put("labels", service.getMetadata().getLabels()==null?"":service.getMetadata().getLabels().toString());
					map.put("selector", getSelector(service)==null?"":getSelector(service).toString());
					map.put("ports", getPorts(service)==null?"":getPorts(service).toString());
					arr.put(map);
				}
			}
		}
		return arr.toString();
	}
	
	@RequestMapping(value="/createservice")
	public String createService() {
		File file=new File("C:\\uploadfiles\\apollo.yml");
		try {
			FileInputStream fis=new FileInputStream(file);
			Pod pod=kube.pods().inNamespace("default").load(file).createOrReplaceWithNew().done();
			logger.info("Pod name: ", KubernetesHelper.getName(pod));
			
//			DoneableService dService=kube.services().inNamespace("default").load(fis).createOrReplaceWithNew();
//			logger.info("Service kind {}", dService.getKind());
//			Service service=dService.done();
//			logger.info("Service {}", KubernetesHelper.getName(service));
//			if (lst!=null) {
//				Service service=null;
//				for (int i=0;i<lst.size();i++) {
//					if (lst.get(i) instanceof Service) {
//						service=(Service) lst.get(i);
//						logger.info("Service {}", KubernetesHelper.getName(service));
//					}
//				}
//			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return "DONE";
	}
}
