package com.lcl.scs.gcp.model;

import com.google.cloud.storage.BlobId;

import lombok.Getter;
import lombok.Setter;

public class GcpFile {
	private BlobId blobId;
	private String content;
	private String fileExt;
	@Getter
	@Setter
	private String fileName;
	private String fileNameWithoutPath;
	private String parentFolder;
	private String path;

	public BlobId getBlobId() {
		return blobId;
	}

	public String getContent() {
		return content;
	}

	public String getFileExt() {
		return fileExt;
	}

	public String getFileName() {
		return fileName;
	}

	public String getFileNameWithoutPath() {
		return fileNameWithoutPath;
	}

	public String getParentFolder() {
		return parentFolder;
	}

	public String getPath() {
		return path;
	}

	public void setBlobId(BlobId blobId) {
		this.blobId = blobId;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setFileExt(String fileExt) {
		this.fileExt = fileExt;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setFileNameWithoutPath(String fileNameWithoutPath) {
		this.fileNameWithoutPath = fileNameWithoutPath;
	}

	public void setParentFolder(String parentFolder) {
		this.parentFolder = parentFolder;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
