package com.DOCKin.global.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;

@Component
@Slf4j
public class AudioConverter {
    public File convertToWav(MultipartFile multipartFile) throws IOException, InterruptedException {

        //임시 파일 생성
        File tempInput = File.createTempFile("stt_input_", "_" + multipartFile.getOriginalFilename());
        multipartFile.transferTo(tempInput);

        // 결과 파일 경로 설정
        File tempOutput = File.createTempFile("stt_output_", ".wav");

        //FFmpeg 명령어 실행
        List<String> command = List.of(
                "ffmpeg", "-y",
                "-i", tempInput.getAbsolutePath(),
                "-acodec", "pcm_s16le",
                "-ar", "16000",
                "-ac", "1",
                tempOutput.getAbsolutePath()
        );

        ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectErrorStream(true);
        Process process = pb.start();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                log.debug("FFmpeg Log: {}", line);
            }
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("FFmpeg 변환 실패. Exit Code: " + exitCode);
        }
        tempInput.delete();

        return tempOutput;
    }
}
