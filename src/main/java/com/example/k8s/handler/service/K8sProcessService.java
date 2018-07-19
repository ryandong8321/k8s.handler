package com.example.k8s.handler.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.k8s.handler.Interfaces.StorageUtils;

import io.fabric8.kubernetes.api.KubernetesHelper;
import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.api.model.NFSVolumeSource;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodSpec;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.ServiceList;
import io.fabric8.kubernetes.api.model.Volume;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.client.KubernetesClient;

@Component
public class K8sProcessService {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Value("${k8s.namespace.default}")
	private String _DEFAULT_NAMESPACE;
	
	@Value("${nfs.prefix}")
	private String _DEFAULT_NFS_PATH;
	
	@Value("${nfs.serverIp}")
	private String _DEFAULT_NFS_IP;

	@Autowired
	private KubernetesClient kube;
	
	@Autowired
	private StorageUtils storageUtils;
	
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
	
	public void updateService(String serviceName, InputStream yamlStream) throws Exception {
		ServiceList sevList=kube.services().inNamespace(_DEFAULT_NAMESPACE).list();
		if (sevList!=null) {
			for (Service service:sevList.getItems()) {
				logger.info("current Service name {}", service.getMetadata().getName());
				if (service!=null&&KubernetesHelper.getName(service).equals(serviceName)) {
					//delete service
					kube.services().inNamespace(_DEFAULT_NAMESPACE).withLabel("app", service.getMetadata().getLabels().get("app")).delete();
					
					//create service
					Service result=kube.services().inNamespace(_DEFAULT_NAMESPACE).load(yamlStream).createOrReplaceWithNew().done();
					logger.info("update Service success {}", result.getMetadata().getName());
					break;
				}
			}
		}
	}
	
	public void createDeployment(String yamlFilePath) throws FileNotFoundException {
		createDeployment(new FileInputStream(new File(yamlFilePath)));
	}
	
	public void createDeployment(InputStream is) throws FileNotFoundException {
		List<HasMetadata> resources = kube.load(is).get();
		if (resources.isEmpty()) {
			logger.info("yaml file is empty");
			return;
		}
		
		for (HasMetadata resource:resources) {
			if (resource instanceof Deployment) {
				Deployment deployment=(Deployment)resource;
				
				PodSpec spec=deployment.getSpec().getTemplate().getSpec();
				
				List<Volume> lstVolume=spec.getVolumes();
				boolean addNFSVolume=true;
				if (lstVolume!=null&&!lstVolume.isEmpty()) {
					for (Volume vol:lstVolume) {
						if (vol!=null&&vol.getName()!=null&&vol.getName().equals("nfs")) {
							addNFSVolume=false;
							break;
						}
					}
				}
				
				if (addNFSVolume) {
					Volume volume=new Volume();
					volume.setName("nfs");
					String nfsPath="/"+_DEFAULT_NFS_PATH+"/"+_DEFAULT_NAMESPACE+"/"+deployment.getMetadata().getName();
					logger.info("nfs path is [{}]", nfsPath);
					NFSVolumeSource nfs=new NFSVolumeSource(nfsPath, false, _DEFAULT_NFS_IP);
					volume.setNfs(nfs);
					spec.setVolumes(java.util.Arrays.asList(volume));
					deployment.getSpec().getTemplate().setSpec(spec);
					
					String createDir="/"+_DEFAULT_NAMESPACE+"/"+deployment.getMetadata().getName();
					logger.info("create nfs path [{}]", createDir);
					storageUtils.mkdir(createDir);
					logger.info("create nfs path [{}] done", createDir);
				}
				Deployment result=kube.apps().deployments().inNamespace(_DEFAULT_NAMESPACE).createOrReplace(deployment);
				logger.info("create deployment success {}", result.getMetadata().getName());
			}else if (resource instanceof Pod){
				Pod pod=(Pod)resource;
				PodSpec spec=pod.getSpec();
				
				List<Volume> lstVolume=spec.getVolumes();
				boolean addNFSVolume=true;
				if (lstVolume!=null&&!lstVolume.isEmpty()) {
					for (Volume vol:lstVolume) {
						if (vol!=null&&vol.getName()!=null&&vol.getName().equals("nfs")) {
							addNFSVolume=false;
							break;
						}
					}
				}
				
				if (addNFSVolume) {
					Volume volume=new Volume();
					volume.setName("nfs");
					String nfsPath="/"+_DEFAULT_NFS_PATH+"/"+_DEFAULT_NAMESPACE+"/"+pod.getMetadata().getName();
					logger.info("nfs path is [{}]", nfsPath);
					NFSVolumeSource nfs=new NFSVolumeSource(nfsPath, false, _DEFAULT_NFS_IP);
					volume.setNfs(nfs);
					spec.setVolumes(java.util.Arrays.asList(volume));
					pod.setSpec(spec);
					
					String createDir="/"+_DEFAULT_NAMESPACE+"/"+pod.getMetadata().getName();
					logger.info("create nfs path [{}]", createDir);
					storageUtils.mkdir(createDir);
					logger.info("create nfs path [{}] done", createDir);
				}
				Pod result=kube.pods().inNamespace(_DEFAULT_NAMESPACE).createOrReplace(pod);
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
