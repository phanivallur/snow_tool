package sgc;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

public class DBLogFilePath {
	
	private int port;
	private String fileType;
	private String filePath;
	private String fileSize;
	private LocalDateTime fileModified;
	
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getFileSize() {
		return fileSize;
	}
	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}
	public LocalDateTime getFileModified() {
		return fileModified;
	}
	public void setFileModified(LocalDateTime fileModified) {
		this.fileModified = fileModified;
	}
}
