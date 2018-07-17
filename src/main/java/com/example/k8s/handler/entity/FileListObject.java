package com.example.k8s.handler.entity;

import java.io.Serializable;

import org.springframework.web.multipart.MultipartFile;

public class FileListObject implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private MultipartFile yamlFile;
	
	private MultipartFile[] confFile;
	
	public FileListObject() {
	}
	
	public FileListObject(MultipartFile yamlFile) {
		this.yamlFile=yamlFile;
	}
	
	public FileListObject(MultipartFile yamlFile, MultipartFile[] confFile) {
		this.yamlFile=yamlFile;
		this.confFile=confFile;
	}

	public MultipartFile getYamlFile() {
		return yamlFile;
	}

	public void setYamlFile(MultipartFile yamlFile) {
		this.yamlFile = yamlFile;
	}

	public MultipartFile[] getConfFile() {
		return confFile;
	}

	public void setConfFile(MultipartFile[] confFile) {
		this.confFile = confFile;
	}
}
