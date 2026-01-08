package com.DOCKin.controller;

import com.DOCKin.dto.WorkLogsCreateRequestDto;
import com.DOCKin.dto.WorkLogsUpdateRequestDto;
import com.DOCKin.dto.Work_logsDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name="작업일지 CRUD",description = "작업일지 CRUD가 가능함")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/work-logs")
public class WorkLogsController {
    //특정 작업자 작업일지 생성 api
    @PostMapping
    public ResponseEntity<Work_logsDto> createWorkLog(@RequestBody WorkLogsCreateRequestDto request){
        return ResponseEntity.status(HttpStatus.CREATED).body(new Work_logsDto());
    }

    //전체 작업일지 조회 api
    @GetMapping
    public ResponseEntity<List<Work_logsDto>> getWorkLog(){
        return ResponseEntity.ok(null);
    }

    //특정 작업자 작업일지 조회 api
    @GetMapping("/{logId}")
    public ResponseEntity<Work_logsDto> getMyWorkLog(@PathVariable Long logId){
        return ResponseEntity.ok(new Work_logsDto());
    }

    //특정 작업자 작업일지 수정 api
    @PutMapping("/{logId}")
    public ResponseEntity<Work_logsDto> PutMyWorkLog(@PathVariable Long logId,
                                                     @RequestBody WorkLogsUpdateRequestDto request){
        return ResponseEntity.ok(new Work_logsDto());
    }

    //특정 작업자 작업일지 삭제 api
    @DeleteMapping("/{logId}")
    public ResponseEntity<Void> DeleteMyWorkLog(@PathVariable("logId") Long logId){
        return ResponseEntity.noContent().build();
    }
}
