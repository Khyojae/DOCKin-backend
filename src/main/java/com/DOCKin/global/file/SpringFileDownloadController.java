package com.DOCKin.global.file;

import org.springframework.core.io.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/spring-file")
@RequiredArgsConstructor
public class SpringFileDownloadController {
    private final SpringFileDownloadService springFileDownloadService;

    @GetMapping("/download")
    public Resource downloadFile(@RequestBody String objectKey,
                                 HttpServletResponse response){
        return springFileDownloadService.getFileResource(objectKey, response);
    }
}
