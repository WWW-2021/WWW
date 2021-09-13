package com.ssafy.db.repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.ssafy.db.entity.QCourseReview.courseReview;

@RequiredArgsConstructor
@Repository
public class CourseReviewQueryRepository {
    private final JPAQueryFactory queryFactory;

    // 코스의 평균 점수 0~10점
    public List<Double> findAvgScoreByCourseId(String courseId) {
        return queryFactory.select(courseReview.score.avg().as("score"))
                .from(courseReview)
                .where(courseReview.courseId.eq(courseId))
                .fetch();
    }
}
