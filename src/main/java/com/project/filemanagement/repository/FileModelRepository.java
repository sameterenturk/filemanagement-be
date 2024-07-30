package com.project.filemanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.filemanagement.model.FileModel;

public interface FileModelRepository extends JpaRepository<FileModel, Long> {
}