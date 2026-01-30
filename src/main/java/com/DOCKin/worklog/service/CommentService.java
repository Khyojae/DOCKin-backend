package com.DOCKin.worklog.service;

import com.DOCKin.worklog.dto.CommentCreateRequestDto;
import com.DOCKin.worklog.dto.CommentResponseDto;
import com.DOCKin.worklog.dto.CommentUpdateRequestDto;
import com.DOCKin.global.error.BusinessException;
import com.DOCKin.global.error.ErrorCode;
import com.DOCKin.member.model.Member;
import com.DOCKin.member.model.UserRole;
import com.DOCKin.worklog.model.Comment;
import com.DOCKin.worklog.model.Work_logs;
import com.DOCKin.member.repository.MemberRepository;
import com.DOCKin.worklog.repository.CommentRepository;
import com.DOCKin.worklog.repository.Work_logsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class CommentService {
    private final CommentRepository commentRepository;
    private final Work_logsRepository workLogsRepository;
    private  final MemberRepository memberRepository;

    //댓글 생성
    @Transactional
    public CommentResponseDto createComment(Long logId,String userId,CommentCreateRequestDto dto){
        Work_logs work_logs = workLogsRepository.findById(logId)
                .orElseThrow(()->new BusinessException(ErrorCode.LOG_NOT_FOUND));

        Member member = memberRepository.findByUserId(userId)
                .orElseThrow(()->new BusinessException(ErrorCode.USER_NOT_FOUND));

        if(member.getRole().equals(UserRole.USER)){
            throw new BusinessException(ErrorCode.NOT_COMMENT_AUTHOR);
        }

        Comment comment = Comment.builder()
                .logId(work_logs)
                .userId(member)
                .content(dto.getContent())
                .build();

        Comment saved = commentRepository.save(comment);
        return CommentResponseDto.from(saved);
    }

    //댓글 수정
    @Transactional
    public CommentResponseDto updateComment(Long logId, String userId,Long commentId, CommentUpdateRequestDto dto){
       workLogsRepository.findById(logId)
               .orElseThrow(()->new BusinessException(ErrorCode.LOG_NOT_FOUND));

        Comment comment =commentRepository.findById(commentId)
                .orElseThrow(()->new BusinessException(ErrorCode.COMMENT_NOT_FOUND));

        if(!comment.getLogId().getLogId().equals(logId)){
            throw new BusinessException(ErrorCode.COMMENT_NOT_FOUND);
        }

        if(!comment.getUserId().getUserId().equals(userId)){
            throw new BusinessException(ErrorCode.NOT_COMMENT_AUTHOR);
        }

        comment.updateContent(dto.getContent());

        return CommentResponseDto.from(comment);
    }

    //댓글 조회
    @Transactional(readOnly = true)
    public List<CommentResponseDto> readComment(Long logId){
        if(!workLogsRepository.existsById(logId)){
            throw new BusinessException(ErrorCode.LOG_NOT_FOUND);
        }

        List<Comment> comments = commentRepository.findAllByLogId_LogId(logId);
        return comments.stream()
                .map(CommentResponseDto::from)
                .collect(Collectors.toList());
    }

    //댓글 삭제
    @Transactional
    public void deleteComment(Long logId, Long commentId,String userId){
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()->new BusinessException(ErrorCode.COMMENT_NOT_FOUND));

        if(!comment.getUserId().getUserId().equals(userId)){
            throw new BusinessException(ErrorCode.NOT_COMMENT_AUTHOR);
        }

        if(!workLogsRepository.existsById(logId)){
            throw new BusinessException(ErrorCode.LOG_NOT_FOUND);
        }

        commentRepository.delete(comment);
    }

}
