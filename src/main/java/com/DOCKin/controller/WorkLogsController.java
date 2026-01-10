package com.DOCKin.controller;

import com.DOCKin.dto.WorkLogsCreateRequestDto;
import com.DOCKin.dto.WorkLogsUpdateRequestDto;
import com.DOCKin.dto.Work_logsDto;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary="특정 작업자 작업일지 생성",description = "특정 작업자의 작업일지를 생성해줌")
    @PostMapping
    public ResponseEntity<Work_logsDto> createWorkLog(@RequestBody WorkLogsCreateRequestDto request){
        return ResponseEntity.status(HttpStatus.CREATED).body(new Work_logsDto());
    }

    @Operation(summary="전체 작업일지 조회",description = "전체 작업자의 작업일지를 조회해줌")
    @GetMapping
    public ResponseEntity<List<Work_logsDto>> getWorkLog(){
        return ResponseEntity.ok(null);
    }

    @Operation(summary="특정 작업자 작업일지 조회",description = "특정 작업자의 작업일지를 조회해줌")
    @GetMapping("/{logId}")
    public ResponseEntity<Work_logsDto> getMyWorkLog(@PathVariable Long logId){
        return ResponseEntity.ok(new Work_logsDto());
    }

    @Operation(summary="특정 작업자 작업일지 수정",description = "특정 작업자의 작업일지를 수정해줌")
    @PutMapping("/{logId}")
    public ResponseEntity<Work_logsDto> PutMyWorkLog(@PathVariable Long logId,
                                                     @RequestBody WorkLogsUpdateRequestDto request){
        return ResponseEntity.ok(new Work_logsDto());
    }

    @Operation(summary="특정 작업자 작업일지 삭제",description = "특정 작업자의 작업일지를 삭제해줌")
    @DeleteMapping("/{logId}")
    public ResponseEntity<Void> DeleteMyWorkLog(@PathVariable("logId") Long logId){
        return ResponseEntity.noContent().build();
    }
}
