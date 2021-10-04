package com.ssafy.api.service;

import com.google.common.primitives.Ints;
import com.google.gson.JsonObject;
import com.querydsl.core.Tuple;
import com.ssafy.api.response.main.GetRankRes;
import com.ssafy.api.response.main.GetRecommendListRes;
import com.ssafy.api.response.main.TodayWalkTimeRes;
import com.ssafy.db.entity.Course;
import com.ssafy.db.entity.CourseFinish;
import com.ssafy.db.entity.User;
import com.ssafy.db.entity.Walk;
import com.ssafy.db.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class MainServiceImpl implements MainService {

    @Autowired
    WalkRepository walkRepository;
    @Autowired
    WalkQueryRepository walkQueryRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CourseRepository courseRepository;
    @Autowired
    CourseReviewQueryRepository courseReviewQueryRepository;
    @Autowired
    CourseLikeQueryRepository courseLikeQueryRepository;
    @Autowired
    CourseFinishQueryRepository courseFinishQueryRepository;
    @Autowired
    CourseFinishRepository courseFinishRepository;

    @Override
    public GetRankRes getRank(){
        int i=0;
        try {
            List<Tuple> rankInfo = walkQueryRepository.findTop3UserByDistance();
            String[] ranking = new String[rankInfo.size()];
            for (Tuple row : rankInfo){
                ranking[i++] = row.get(1,String.class);//
            }
            GetRankRes resbody = new GetRankRes();
            resbody.setRanking(ranking);
            return resbody;
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public TodayWalkTimeRes getTodayWalk(String nickname, String date){
        String userId = userRepository.findByNickname(nickname).getUserId();
        List<Integer> walklist = walkQueryRepository.TodayWalkTime(userId,date);
        if (walklist.size()==0){//오늘 걸은 기록이 없음
            TodayWalkTimeRes resbody = new TodayWalkTimeRes();
            resbody.setSecond(0);
            return resbody;
        }
        int totalTime = 0;
        for(int i=0;i<walklist.size();i++){
            totalTime += walklist.get(i);
        }
        TodayWalkTimeRes resbody = new TodayWalkTimeRes();
        resbody.setSecond(totalTime);
        return resbody;
    }

    @Override//calorie : 프론트에서 계산해서 주거나 거리랑 시간만 보낼시 백에서 계산 예정
    public boolean finishRecord(String userId, int courseId, double distance, int time, int calorie){
        User user = userRepository.findByUserId(userId);
        Course course = courseRepository.findByCourseId(courseId);

        try {
            Walk walk = new Walk(0,user,course,distance,time,calorie);
            CourseFinish courseFinish = new CourseFinish(user,course);
            walkRepository.save(walk);
            courseFinishRepository.save(courseFinish);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public GetRecommendListRes getRecommendList(String sigu){
        try{
            // 좋아요 많은 순, 리뷰, 다른 사용자가 많이 산책한 코스
            List<Integer> bestCourses = courseLikeQueryRepository.findTop5CourseByLike(sigu);
            List<Integer> bestReviews = courseReviewQueryRepository.findTop5ReviewsByScore(sigu);
            List<Integer> bestFinishes = courseFinishQueryRepository.findTop5CourseByCnt(sigu);
            bestCourses.addAll(bestReviews);
            bestCourses.addAll(bestFinishes);

            List<Integer> setBestList = bestCourses.stream().distinct().collect(Collectors.toList());

            int[] recommends = Ints.toArray(setBestList);

            GetRecommendListRes resbody = new GetRecommendListRes();
            resbody.setRecommendList(recommends);
            return resbody;
        }catch (Exception e){
            System.out.println(e);
            return null;
        }
    }
}
