package com.DOCKin.model.Attendance;

import com.DOCKin.model.Member.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name="attendance")
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class Attendance {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch =FetchType.LAZY)
    @JoinColumn(name="user_id",nullable=false)
    private Member member;

    @Column(name="clock_in_time",nullable=false)
    private LocalDateTime clockInTime;

    @Column(name="clock_out_time")
    private LocalDateTime clockOutTime;

    @Column(name="work_date",nullable = false)
    private LocalDate workDate;

    @Enumerated(EnumType.STRING) //NORMAL, LATE, ABSENT, VACATION, SICK
    @Column(name="status",length=20,nullable=false)
    private AttendanceStatus status;

    @Column(name="in_location")
    private String inLocation;

    @Column(name="out_location")
    private String outLocation;

    public void recordClockOut(LocalDateTime outTime, String location, AttendanceStatus status){
        this.clockOutTime = outTime;
        this.outLocation = location;
    }
}
