package com.DOCKin.global.file;

import org.springframework.core.io.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/spring-file")
@RequiredArgsConstructor
public class SpringFileDownloadController {
    private final SpringFileDownloadService springFileDownloadService;

    @PostMapping
    public Resource downloadFile(@RequestBody FileDownloadRequest request,
                                 HttpServletResponse response){
        return springFileDownloadService.getFileResource(request.objectKey(),
                response);
    }
}
