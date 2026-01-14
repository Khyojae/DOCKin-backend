package com.DOCKin.model.Attendance;

import com.DOCKin.model.Member.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static java.time.Period.between;

@Entity
@Table(name="attendance")
@Builder
@Getter
@Setter
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

    @Column(name="total_work_time")
    private String totalWorkTime;

    public void recordClockOut(LocalDateTime outTime, String location, AttendanceStatus status){
        this.clockOutTime = outTime;
        this.outLocation = location;

        if(this.clockOutTime!=null){
            Duration duration = Duration.between(this.clockInTime,outTime);
            long hours = duration.toHours();
            long minutes = duration.toMinutesPart();
            long seconds = duration.toSecondsPart();

            this.totalWorkTime = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        }
    }
}
