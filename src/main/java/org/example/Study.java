package org.example;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Study {

    private StudyStatus status = StudyStatus.DRAFT;

    private int limit;

    public int testLimit(){
        if(limit < 0 ){
            throw new IllegalArgumentException("limit은 0보다 커야한다.");
        }else{
            return this.limit;
        }
    }
}
