package com.project.filemanagement.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.project.filemanagement.model.FileModel;
import com.project.filemanagement.repository.FileModelRepository;

@Service
public class FileService {
	private static final String UPLOAD_DIR = "uploads/";

	@Autowired
	private FileModelRepository repository;

	public FileModel saveFile(MultipartFile file) throws IOException {
		if (file.getSize() > 5 * 1024 * 1024) {
			throw new IllegalArgumentException("File size exceeds limit");
		}

		String extension = getFileExtension(file.getOriginalFilename());
		if (!isValidExtension(extension)) {
			throw new IllegalArgumentException("Invalid file extension");
		}

		File dir = new File(UPLOAD_DIR);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		Path path = Paths.get(UPLOAD_DIR + file.getOriginalFilename());
		Files.write(path, file.getBytes());

		FileModel metadata = new FileModel();
		metadata.setName(file.getOriginalFilename());
		metadata.setExtension(extension);
		metadata.setPath(path.toString());
		metadata.setSize(file.getSize());

		return repository.save(metadata);
	}

	public List<FileModel> listFiles() {
		return repository.findAll();
	}

	public FileModel getFile(Long id) {
		Optional<FileModel> fileMetadata = repository.findById(id);
		if (fileMetadata.isPresent()) {
			return fileMetadata.get();
		} else {
			throw new IllegalArgumentException("File not found");
		}
	}

	public byte[] getFileContent(Long id) throws IOException {
		FileModel metadata = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("File not found"));
		return Files.readAllBytes(Paths.get(metadata.getPath()));
	}

	public void deleteFile(Long id) {
		FileModel metadata = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("File not found"));
		File file = new File(metadata.getPath());
		if (file.exists()) {
			file.delete();
		}
		repository.deleteById(id);
	}

	private boolean isValidExtension(String extension) {
		return extension.equals("png") || extension.equals("jpeg") || extension.equals("jpg")
				|| extension.equals("docx") || extension.equals("pdf") || extension.equals("xlsx")
				|| extension.equals("txt");
	}

	private String getFileExtension(String filename) {
		return filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
	}
}
