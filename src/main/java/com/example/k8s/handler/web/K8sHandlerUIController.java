package com.example.k8s.handler.web;

import java.io.File;
import java.io.FileNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.example.k8s.handler.entity.FileListObject;
import com.example.k8s.handler.service.K8sProcessService;

@Controller
public class K8sHandlerUIController {
	
	protected final Logger logger=LoggerFactory.getLogger(getClass());
	
	private final String _UPLOADFILESPATH="C:/uploadfiles/";
	private final String _YAMLFILESPATH="yaml/";
	
	@Autowired
	private K8sProcessService k8sProcessService;
	
    @RequestMapping("file")
    public String file(){
        return "/fileupload";
    }
    
    /**
     * 实现文件上传
     * */
    @RequestMapping("fileUpload")
    @ResponseBody 
    public String fileUpload(@RequestParam("yamlFile") MultipartFile yamlFile, @RequestParam("confFile") MultipartFile[] confFile, @RequestParam("pathName") String[] pathName){
//    	if (fileInfo!=null&&fileInfo.length>0) {
//    		for (FileListObject fileObject:fileInfo) {
//    			MultipartFile file=fileObject.getYamlFile();
//    			
//    			if(file.isEmpty()){
//    	            return "false";
//    	        }
//    	        String fileName = file.getOriginalFilename();
//    	        int size = (int) file.getSize();
//    	        System.out.println(fileName + "-->" + size);
//    	        
//    	        String path = "C:/uploadfiles" ;
//    	        File dest = new File(path + "/" + fileName);
//    	        if(!dest.getParentFile().exists()){ //判断文件父目录是否存在
//    	            dest.getParentFile().mkdir();
//    	        }
//    	        try {
//    	            file.transferTo(dest); //保存文件
//    	            return "true";
//    	        } catch (IllegalStateException e) {
//    	            // TODO Auto-generated catch block
//    	            e.printStackTrace();
//    	            return "false";
//    	        } catch (IOException e) {
//    	            // TODO Auto-generated catch block
//    	            e.printStackTrace();
//    	            return "false";
//    	        }
//    		}
//    	}
    	
    	String fileName=null;
    	if (yamlFile!=null) {
    		fileName=yamlFile.getOriginalFilename();
    		int size = (int) yamlFile.getSize();
    		logger.info("yaml file size is {}", size);
    		String path = _UPLOADFILESPATH + _YAMLFILESPATH;
    		
    	}
    	
    	if (confFile!=null&&fileName!=null&&!fileName.equals("")) {
    		String confName=null;
    		for (MultipartFile conf:confFile) {
    			confName=conf.getOriginalFilename();
    			int size = (int) conf.getSize();
        		logger.info("yaml file size is {}", size);
        		String path = _UPLOADFILESPATH + File.pathSeparator + fileName.substring(0, fileName.lastIndexOf("."));
    		}
    	}
    	
    	try {
			k8sProcessService.createDeployment(null);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return "true";
    }
    
    @RequestMapping("updatefileupload")
    @ResponseBody 
    public String fileUpload(@RequestParam("serviceName") String serviceName, @RequestParam("confFile") MultipartFile[] confFile, @RequestParam("pathName") String[] pathName){
//    	if (fileInfo!=null&&fileInfo.length>0) {
//    		for (FileListObject fileObject:fileInfo) {
//    			MultipartFile file=fileObject.getYamlFile();
//    			
//    			if(file.isEmpty()){
//    	            return "false";
//    	        }
//    	        String fileName = file.getOriginalFilename();
//    	        int size = (int) file.getSize();
//    	        System.out.println(fileName + "-->" + size);
//    	        
//    	        String path = "C:/uploadfiles" ;
//    	        File dest = new File(path + "/" + fileName);
//    	        if(!dest.getParentFile().exists()){ //判断文件父目录是否存在
//    	            dest.getParentFile().mkdir();
//    	        }
//    	        try {
//    	            file.transferTo(dest); //保存文件
//    	            return "true";
//    	        } catch (IllegalStateException e) {
//    	            // TODO Auto-generated catch block
//    	            e.printStackTrace();
//    	            return "false";
//    	        } catch (IOException e) {
//    	            // TODO Auto-generated catch block
//    	            e.printStackTrace();
//    	            return "false";
//    	        }
//    		}
//    	}
    	
    	if (confFile!=null) {
    		String confName=null;
    		for (MultipartFile conf:confFile) {
    			confName=conf.getOriginalFilename();
    			int size = (int) conf.getSize();
        		logger.info("yaml file size is {}", size);
        		String path = _UPLOADFILESPATH + File.pathSeparator + serviceName;
    		}
    	}
    	
    	try {
			k8sProcessService.updateService(serviceName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return "true";
    }
    
    /**
     * 实现文件上传
     * */
    @RequestMapping("/v1/fileUpload")
    @ResponseBody 
    public String fileUploadObj(@ModelAttribute("fileListObject") FileListObject[] lst){
    	if (lst!=null) {
    		for (FileListObject item:lst) {
    			logger.info("file {}", item.getYamlFile().getName());
    		}
    	}
    	return "true";
    }

}
