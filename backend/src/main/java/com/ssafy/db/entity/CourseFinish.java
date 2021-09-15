package com.ssafy.db.entity;

import com.ssafy.db.BaseTimeEntity;
import com.ssafy.db.key.CoursePK;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@IdClass(CoursePK.class)
@Table(name="course_finish")
public class CourseFinish extends BaseTimeEntity {

    @Id
    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name="user_id")
    private User user;

    @Id
    @ManyToOne(targetEntity = Course.class)
    @JoinColumn(name="course_id")
    private Course course;

}