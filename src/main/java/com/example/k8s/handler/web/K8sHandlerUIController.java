package com.example.k8s.handler.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.example.k8s.handler.entity.FileListObject;

@Controller
public class K8sHandlerUIController {
	
	protected final Logger logger=LoggerFactory.getLogger(getClass());
	
    @RequestMapping("file")
    public String file(){
        return "/fileupload";
    }
    
    /**
     * 实现文件上传
     * */
    @RequestMapping("fileUpload")
    @ResponseBody 
    public String fileUpload(@RequestParam("yamlFile") MultipartFile yamlFile, @RequestParam("yamlFile") MultipartFile[] confFile, @RequestParam("pathName") String[] pathName){
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
    	return "true";
    }

}
