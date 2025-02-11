package springgradle.bankingproject.features.fileupload;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/v1/upload")
@RequiredArgsConstructor
public class FileUploadController {
    private final FileUploadService fileUploadService;
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    FileUploadResponse upload(@RequestPart MultipartFile file) {
        System.out.println(file.getContentType());
        System.out.println(file.getOriginalFilename());
        System.out.println(file.getSize());
        return fileUploadService.upload(file);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/multiple")
    List<FileUploadResponse> uploadMultiple(@RequestPart List<MultipartFile> files) {
        return fileUploadService.uploadMultiple(files);
    }
    @DeleteMapping("/{fileName}")
    void deleteFile(@PathVariable String fileName) {
        fileUploadService.deleteFile(fileName);
    }
}
