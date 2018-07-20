package com.example.k8s.handler.web;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.k8s.handler.Interfaces.StorageUtils;
import com.example.k8s.handler.service.K8sProcessService;

@Controller
public class K8sHandlerUIController {
	
	protected final Logger logger=LoggerFactory.getLogger(getClass());
	
	private final String _NAMESPACE_DEFAULT="/default";
	
	@Autowired
	private K8sProcessService k8sProcessService;
	
	@Autowired
	private StorageUtils storageUtils;
	
    @RequestMapping("file")
    public String file(){
        return "/fileupload";
    }
    
    @RequestMapping("createinstance")
    public String createInstance(){
        return "/create";
    }
    
    @RequestMapping("updateinstance")
    public String updateInstance(){
        return "/update";
    }
    
    @RequestMapping("fileUpload")
    public void fileUpload(HttpServletResponse response, @RequestParam("yamlFile") MultipartFile yamlFile, @RequestParam("confFile") MultipartFile[] confFile, @RequestParam("pathName") String[] pathName){
    	String fileName=null;
    	boolean canCreate=false;
    	if (yamlFile!=null) {
    		fileName=yamlFile.getOriginalFilename();
    		logger.info("yaml file name [{}]", fileName);
    		canCreate=true;
    	}
    	
    	if (confFile!=null&&confFile.length>0&&fileName!=null&&!fileName.equals("")) {
    		String confName=null, remotePath=_NAMESPACE_DEFAULT+"/"+fileName.substring(0, fileName.lastIndexOf("."));
    		MultipartFile conf=null;
    		for (int idx=0;idx<confFile.length;idx++) {
    			conf=confFile[idx];
    			if (conf.getSize()==0) {
    				continue;
    			}
    			if (pathName==null||pathName.length<1||pathName[idx]==null||pathName[idx].equals("")) {
    				remotePath+="/";
    			}else {
    				remotePath+="/"+pathName[idx];
    			}
    			
    			confName=conf.getOriginalFilename();
    			logger.info("upload conf file [{}] and remote path [{}]", confName, remotePath);
    			int size = (int) conf.getSize();
        		try {
        			logger.info("upload conf file [{}] to nfs server and size is [{}]", confName, size);
        			canCreate=storageUtils.save(conf.getInputStream(), remotePath, confName);
    				logger.info("upload conf file [{}] to nfs server and size is [{}] done", confName, size);
    			} catch (IOException e) {
    				logger.info("upload conf file [{}] to nfs server and size is [{}] exception", confName, size);
    				canCreate=false;
    				e.printStackTrace();
    			}
    		}
    	}
    	
    	try {
    		if (canCreate) {
    			logger.info("create deployment pod service in k8s according to yaml file");
    			k8sProcessService.createDeployment(yamlFile.getInputStream());
    			logger.info("create deployment pod service in k8s according to yaml file done");
    		}
		} catch (FileNotFoundException e) {
			logger.info("create deployment pod service in k8s according to yaml file exception");
			e.printStackTrace();
			canCreate=false;
		} catch (IOException e) {
			logger.info("create deployment pod service in k8s according to yaml file exception");
			e.printStackTrace();
			canCreate=false;
		}
    	
    	try {
			response.sendRedirect("/html/create.html?result="+canCreate);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    @RequestMapping("updatefile")
    public void fileUpload(HttpServletResponse response, @RequestParam("yamlFile") MultipartFile yamlFile, @RequestParam("serviceName") String serviceName, @RequestParam("confFile") MultipartFile[] confFile, @RequestParam("pathName") String[] pathName){
    	boolean result=true;
    	if (confFile!=null&&confFile.length>0) {
    		String confName=null, remotePath=_NAMESPACE_DEFAULT+"/"+serviceName;
    		MultipartFile conf=null;
    		for (int idx=0;idx<confFile.length;idx++) {
    			conf=confFile[idx];
    			
    			if (conf.getSize()==0) {
    				continue;
    			}
    			if (pathName==null||pathName.length<1||pathName[idx]==null||pathName[idx].equals("")) {
    				remotePath+="/";
    			}else {
    				remotePath+="/"+pathName[idx];
    			}
    			
    			confName=conf.getOriginalFilename();
    			logger.info("upload conf file [{}] and remote path [{}]", confName, remotePath);
    			int size = (int) conf.getSize();
        		try {
        			logger.info("upload conf file [{}] to nfs server and size is [{}]", confName, size);
    				storageUtils.save(conf.getInputStream(), remotePath, confName);
    				logger.info("upload conf file [{}] to nfs server and size is [{}] done", confName, size);
    			} catch (IOException e) {
    				logger.info("upload conf file [{}] to nfs server and size is [{}] exception", confName, size);
    				result=false;
    				e.printStackTrace();
    			}
    		}
    	}
    	
    	try {
    		if (yamlFile!=null&&yamlFile.getSize()>0) {
    			k8sProcessService.updateService(serviceName, yamlFile.getInputStream());
    		}else {
    			k8sProcessService.updateService(serviceName);
    		}
		} catch (Exception e) {
			e.printStackTrace();
			result=false;
		}
    	try {
			response.sendRedirect("/html/update.html?result="+result);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
