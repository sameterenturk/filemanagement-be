package com.project.filemanagement.dto;

import lombok.Data;

@Data
public class FileDto {

	private String name;
	private String extension;
	private String path;
	private long size;
}