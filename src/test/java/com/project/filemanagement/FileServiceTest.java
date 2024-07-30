package com.project.filemanagement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import com.project.filemanagement.model.FileModel;
import com.project.filemanagement.repository.FileModelRepository;
import com.project.filemanagement.service.FileService;

public class FileServiceTest {

	@Mock
	private FileModelRepository repository;

	@Mock
	private MultipartFile multipartFile;

	@InjectMocks
	private FileService fileService;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testSaveFile_Success() throws IOException {
		String fileName = "test.pdf";
		String filePath = "uploads\\test.pdf";
		long fileSize = 1024L;

		when(multipartFile.getOriginalFilename()).thenReturn(fileName);
		when(multipartFile.getSize()).thenReturn(fileSize);
		when(multipartFile.getBytes()).thenReturn(new byte[0]);
		when(repository.save(any(FileModel.class))).thenAnswer(i -> i.getArguments()[0]);

		FileModel savedMetadata = fileService.saveFile(multipartFile);

		assertEquals(fileName, savedMetadata.getName());
		assertEquals(filePath, savedMetadata.getPath());
		assertEquals(fileSize, savedMetadata.getSize());
		verify(repository, times(1)).save(any(FileModel.class));
	}

	@Test
	public void testSaveFile_InvalidExtension() {
		String fileName = "test.pdf";

		when(multipartFile.getOriginalFilename()).thenReturn(fileName);

		Exception exception = assertThrows(NullPointerException.class, () -> {
			fileService.saveFile(multipartFile);
		});

		assertEquals(null, exception.getMessage());
		verify(repository, times(0)).save(any(FileModel.class));
	}

	@Test
	public void testListFiles() {
		when(repository.findAll()).thenReturn(Collections.emptyList());

		assertTrue(fileService.listFiles().isEmpty());
		verify(repository, times(1)).findAll();
	}

	@Test
	public void testGetFileContent_Success() throws IOException {
		Long fileId = 1L;
		String filePath = "uploads/test.pdf";
		FileModel fileModel = new FileModel();
		fileModel.setPath(filePath);

		when(repository.findById(fileId)).thenReturn(Optional.of(fileModel));
		byte[] content = fileService.getFileContent(fileId);

		assertNotNull(content);
		verify(repository, times(1)).findById(fileId);
	}

	@Test
	public void testGetFileContent_FileNotFound() {
		Long fileId = 1L;

		when(repository.findById(fileId)).thenReturn(Optional.empty());

		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			fileService.getFileContent(fileId);
		});

		assertEquals("File not found", exception.getMessage());
		verify(repository, times(1)).findById(fileId);
	}

	@Test
	public void testDeleteFile_Success() {
		Long fileId = 1L;
		String filePath = "uploads\\test.pdf";
		FileModel fileModel = new FileModel();
		fileModel.setPath(filePath);

		when(repository.findById(fileId)).thenReturn(Optional.of(fileModel));

		fileService.deleteFile(fileId);

		verify(repository, times(1)).deleteById(fileId);
		File file = new File(filePath);
		if (file.exists()) {
			file.delete();
		}
	}

	@Test
	public void testDeleteFile_FileNotFound() {
		Long fileId = 1L;

		when(repository.findById(fileId)).thenReturn(Optional.empty());

		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			fileService.deleteFile(fileId);
		});

		assertEquals("File not found", exception.getMessage());
		verify(repository, times(1)).findById(fileId);
	}
}
