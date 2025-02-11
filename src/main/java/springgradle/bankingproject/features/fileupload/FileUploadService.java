package springgradle.bankingproject.features.fileupload;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileUploadService {
    FileUploadResponse upload(MultipartFile file);
    List<FileUploadResponse> uploadMultiple(List<MultipartFile> files);

    void deleteFile(String fileName);
}
