package com.ssafy.db.repository;

import com.ssafy.db.entity.User;
import com.ssafy.db.entity.Walk;
import com.ssafy.db.key.CoursePK;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface WalkRepository extends JpaRepository<Walk, CoursePK> {

    // 사용자가 걸은 기록 전체 리스트
    public List<Walk> findByUser(User user);

    // (페이징) 사용자가 걸은 기록 코스 전체 리스트
    public Page<Walk> findByUserOrderByCourseId(
            User user, Pageable pageable);

    // 코스별 걸은 기록 개수 조회
    public int countByCourseId(String courseId);

    // 오늘날짜 코스별 걸은 기록 개수 조회
    // 추후 동적 쿼리로 변경 예정
    public int countByCourseIdAndDateAfter(String courseId, LocalDateTime today);

    public List<Walk> findByUserAndDateAfter(User user, LocalDateTime today);

}