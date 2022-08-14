package org.example.domain;

import lombok.*;
import org.example.study.StudyStatus;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Study {

    @Id
    @GeneratedValue
    private Long id;

    private StudyStatus status = StudyStatus.DRAFT;

    private int limit;

    private String name;
    private LocalDateTime openedDateTime;
    private Member owner;

    public Study(int limit, String name){
        this.name = name;
        this.limit = limit;
    }

    public int testLimit(){
        if(limit < 0 ){
            throw new IllegalArgumentException("limit은 0보다 커야한다.");
        }else{
            return this.limit;
        }
    }

    public void open() {
        this.openedDateTime = LocalDateTime.now();
        this.status = StudyStatus.OPENED;
    }
}
