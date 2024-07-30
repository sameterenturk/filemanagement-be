package com.project.filemanagement.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.filemanagement.model.FileModel;
import com.project.filemanagement.service.FileService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/files")
@SecurityRequirement(name = "bearerAuth")
public class FileController {

	@Autowired
	private FileService fileService;

	@PostMapping("/upload")
	public ResponseEntity<FileModel> uploadFile(@RequestParam("file") MultipartFile file) {
		try {
			FileModel metadata = fileService.saveFile(file);
			return ResponseEntity.ok(metadata);
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@GetMapping("/listFiles")
	public ResponseEntity<List<FileModel>> listFiles() {
		List<FileModel> files = fileService.listFiles();
		return ResponseEntity.ok(files);
	}

	@GetMapping("/getFileContent/{id}")
	public ResponseEntity<byte[]> getFileContent(@PathVariable Long id) {
		try {
			byte[] content = fileService.getFileContent(id);
			return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"file\"")
					.contentType(MediaType.APPLICATION_OCTET_STREAM).body(content);
		} catch (IOException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/getFile/{id}")
	public ResponseEntity<FileModel> getFile(@PathVariable Long id) {
		try {
			FileModel fileModel = fileService.getFile(id);
			return ResponseEntity.ok(fileModel);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteFile(@PathVariable Long id) {
		try {
			fileService.deleteFile(id);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}
}