package com.ssafy.db.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.api.request.CourseReq;
import com.ssafy.api.response.user.CourseBody;
import com.ssafy.db.entity.QCourseLike;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.ssafy.db.entity.QCourse.course;
import static com.ssafy.db.entity.QWalk.walk;

@RequiredArgsConstructor
@Repository
public class CourseQueryRepository {
    private final JPAQueryFactory queryFactory;


    //최근 걸은 course
    public List<Tuple> findRecentList(String userId) {
        return queryFactory
                .select(course.courseId.as("courseId"),
                        course.name.as("courseName"),
                        course.address.as("address"),
                        course.distance.as("courseLength"),
                        course.time.as("courseTime")
                )
                .from(walk)
                .join(walk.course,course)
                .where(walk.user.userId.eq(userId))
                .orderBy(walk.date.desc())
                .fetch();
    }

    // 코스 목록 조회 (코스 ID, 코스 이름, 코스 세부 이름, 주소, 코스 길이, 코스 시간, 경도, 위도, 좋아요 수, 로그인 사용자 좋아요 여부, 사용자와 산책지 떨어진 거리)
    // 코스별 평균 리뷰 점수 조회를 위해 leftJoin(CourseReview)을 한번 더 할 경우 SQL 지연 시간이 증가되므로 리뷰 점수는 별도로 쿼리 실행
    public Page<CourseBody> findCourseList(CourseReq courseReq, Pageable pageable) {

        // 코스별 좋아요 개수를 새기 위한 테이블 별칭
        QCourseLike cl = new QCourseLike("cl");

        // 로그인 사용자의 코스별 좋아요 여부를 조회하기 위한 테이블 별칭
        QCourseLike my_cl = new QCourseLike("my_cl");

        // 위도, 경도로 거리 계산 (Double형 KM 단위)
        // 반환형이 숫자일 경우 NumberTemplate, 문자일 경우 StringTemplate 사용
        NumberTemplate geoDistance = Expressions.numberTemplate(Double.class,
                "6371 * acos (" +
                        "cos ( radians({0}) )" +
                        "* cos ( radians({2}) )" +
                        "* cos ( radians({3}) - radians({1}) )" +
                        "+ sin ( radians({0}) )" +
                        "* sin ( radians({2}) )" +
                        ")", course.latitude, course.longitude,
                courseReq.getLatitude(), courseReq.getLongtitude()
        );

        // 정렬 조건: 거리짧은순(기본값), 인기순
        OrderSpecifier orderSpecifier = geoDistance.asc();
        switch(courseReq.getSort()) {
            case "distance": // 거리순 정렬
                orderSpecifier = geoDistance.asc();
                break;
            case "likes": // 인기순(좋아요 개수) 정렬
                orderSpecifier = cl.count().desc();
                break;
        }

        // 쿼리 결과를 DTO로 받을 경우 DTO 변수 이름과 이름이 같아지도록 별칭 지정
        QueryResults<CourseBody> results = queryFactory
                .select(Projections.fields(CourseBody.class,
                        course.courseId.as("courseId"),
                        course.flagName.as("courseFlagName"),
                        course.name.as("courseName"),
                        course.address.as("address"),
                        course.distance.as("courseLength"),
                        course.time.as("time"),
                        course.latitude.as("latitude"),
                        course.longitude.as("longitude"),
                        cl.count().intValue().as("likes"),
                        my_cl.countDistinct().intValue().as("myLike"),
                        geoDistance.as("geoDistance"))
                )
                .from(course)
                .leftJoin(cl).on(cl.course.eq(course))
                .leftJoin(my_cl).on(my_cl.course.eq(course).and(my_cl.user.userId.eq(courseReq.getUserId())))
                .where(course.distance.between(courseReq.getMinDistnace(), courseReq.getMaxDistance())
                        .and(course.timeInt.between(courseReq.getMinTime(), courseReq.getMaxTime()))
                        .and(course.address.contains(courseReq.getDong())))
                .groupBy(course.courseId)
                .orderBy(orderSpecifier, course.courseId.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<CourseBody> data = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(data, pageable, total);
    }

}