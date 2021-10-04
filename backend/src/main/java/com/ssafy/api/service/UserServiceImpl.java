package com.ssafy.api.service;

import com.querydsl.core.Tuple;
import com.ssafy.api.response.user.*;
import com.ssafy.common.model.response.BaseResponseBody;
import com.ssafy.db.entity.User;
import com.ssafy.db.entity.Walk;
import com.ssafy.db.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.List;
import com.ssafy.db.repository.UserRepository;

import java.util.HashMap;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    UserRepository userRepository;

    @Autowired
    WalkQueryRepository walkQueryRepository;

    @Autowired
    WalkRepository walkRepository;

    @Autowired
    CourseLikeQueryRepository courseLikeQueryRepository;

    @Autowired
    CourseQueryRepository courseQueryRepository;

    @Autowired
    CourseReviewQueryRepository courseReviewQueryRepository;

    @Autowired
    RedisService redisService;

    @Override
    public User getUserId(String userId) {
        Optional<User> user = userRepository.findUserByUserId(userId);
        if(!user.isPresent()){
            return null;
        }
        return user.get();
    }

    @Override
    public User createUser(HashMap<String,Object> Token, HashMap<String, Object> userInfo) {
        String accessToken = (String) Token.get("accessToken");
        String refreshToken = (String) Token.get("refreshToken");
        Long accessTokenExpire = Long.parseLong(Token.get("accessTokenExpire").toString());
        Long refreshTokenExpire = Long.parseLong(Token.get("refreshTokenExpire").toString());

        User user = new User();

        String id = (String) userInfo.get("userId");
        String nickname = (String) userInfo.get("nickname");

        user.setUserId(id);
        user.setNickname(nickname);

        // DB에 user 정보 저장
        userRepository.save(user);

        return user;
    }


    @Override
    public BaseResponseBody readUserInfo(String userId) {
        //nickname
        UserResponseBody userResponseBody = new UserResponseBody();
        String nickName = userRepository.findByUserId(userId).getNickname();

        //other
        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd");

        Calendar c = Calendar.getInstance();
        String today = formatter.format(c.getTime());
        c.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);

        LocalDate ld = LocalDate.parse(formatter.format(c.getTime()), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDateTime ldt = ld.atStartOfDay();
        int dayOfWeek = LocalDate.parse(today, DateTimeFormatter.ofPattern("yyyy-MM-dd")).getDayOfWeek().getValue(); // monday = 1, sunday = 7

        User user = new User();
        user.setUserId(userId);

        List<Walk> ret = walkRepository.findByUserAndDateAfter(user, ldt);

        int sumTime=0,sumCalorie=0;
        double sumDistance=0;
        for(Walk w : ret){
            sumTime+=w.getTime();
            sumDistance+=w.getDistance();
            sumCalorie+=w.getCalorie();
        }

        userResponseBody.setStatusCode(200);
        userResponseBody.setMessage("OK");
        userResponseBody.setNickName(nickName);
        userResponseBody.setSumCalorie(sumCalorie);
        userResponseBody.setWeeklySumTime(sumTime);
        userResponseBody.setSumDistance(sumDistance);
        userResponseBody.setDailyAvgTime(sumTime/dayOfWeek);

        return userResponseBody;

    }



    @Override
    public BaseResponseBody readRecentCourse(String userId) {

        List<Tuple> result = courseQueryRepository.findRecentList(userId);
        CourseResponseBody courseResponseBody = new CourseResponseBody();

        try {
            courseResponseBody.setMessage("OK");
            courseResponseBody.setStatusCode(200);

            for (Tuple t : result) {
                System.out.println(t);
                List<Double> scoreL = courseReviewQueryRepository.findAvgScoreByCourseId(t.get(0, Integer.class));
                double score;
                int courseId = t.get(0, Integer.class);
                System.out.println(courseId+" "+userId);
                Object o = courseReviewQueryRepository.findScoreByCourseIdAndUserId(courseId, userId);
                double myScore=0;
                if(o!=null)myScore = (double)o;


                if(scoreL==null || scoreL.size()==0 || scoreL.get(0)==null){
                    score=0;
                }else{
                    score=scoreL.get(0);
                }

                CourseBody courseBody = new CourseBody();
                courseBody.setCourseId(courseId);
                courseBody.setCourseName(t.get(1, String.class));
                courseBody.setAddress(t.get(2, String.class));
                courseBody.setCourseCnt(score);
                courseBody.setCourseLength(t.get(3, Double.class));
                courseBody.setTime(""+t.get(4, Integer.class));
                courseBody.setTimeInt(t.get(5,Integer.class));
                courseBody.setCourseFlagName(t.get(6, String.class));
                courseBody.setCalorie(t.get(7, Integer.class));
                courseBody.setMyScore(myScore);

                courseBody.setLatitude((t.get(8, Double.class)));
                courseBody.setLongitude((t.get(9, Double.class)));

                courseResponseBody.getCourseList().add(courseBody);
            }

            return courseResponseBody;
        }catch (Exception e){
            e.printStackTrace();
            courseResponseBody.setMessage("Not Found");
            courseResponseBody.setStatusCode(404);
            return courseResponseBody;
        }
    }

    @Override
    public BaseResponseBody readWishCourse(String userId) {

        List<Tuple> result = courseLikeQueryRepository.findWishList(userId);
        CourseResponseBody courseResponseBody = new CourseResponseBody();
        try {
            courseResponseBody.setMessage("OK");
            courseResponseBody.setStatusCode(200);
            for (Tuple t : result) {
                System.out.println(t);

                CourseBody courseBody = new CourseBody();
                courseBody.setCourseId(t.get(0, Integer.class));
                courseBody.setCourseName(t.get(1, String.class));
                courseBody.setAddress(t.get(2, String.class));
                courseBody.setCourseCnt(0.0);
                courseBody.setCourseLength(t.get(3, Double.class));
                courseBody.setTime(t.get(4, String.class));
                courseBody.setTimeInt(t.get(5,Integer.class));
                courseBody.setCourseFlagName(t.get(6, String.class));

                courseBody.setLatitude(t.get(7, Double.class));
                courseBody.setLongitude(t.get(8, Double.class));
                courseResponseBody.getCourseList().add(courseBody);
            }

            return courseResponseBody;
        }catch (Exception e){
            e.printStackTrace();
            courseResponseBody.setMessage("Not Found");
            courseResponseBody.setStatusCode(404);
            return courseResponseBody;
        }
    }

    @Override
    public BaseResponseBody readTotalTime(String userId) {

        try {
            int time = -1;
            List<Integer> result = walkQueryRepository.TotalWalkTime(userId);
            if(result.get(0)!=null)return new TotalTimeResponseBody(200, "OK", result.get(0));
            else return new TotalTimeResponseBody(200, "OK", 0);
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public BaseResponseBody readTime(String userId, String type) {

        TimeResponseBody timeResponseBody = new TimeResponseBody();

        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd");

        Calendar c = Calendar.getInstance();

        LocalDate prevStart=null;
        LocalDate prevFin=null;
        int prevDay=0;

        int today=0;
        if("week".equals(type)){
            today = LocalDate.parse(formatter.format(c.getTime()),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd")).getDayOfWeek().getValue(); // monday = 1,

            c.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);

            prevStart = LocalDate.parse(formatter.format(c.getTime())).with(TemporalAdjusters.previous(DayOfWeek.MONDAY));
            prevFin = LocalDate.parse(formatter.format(c.getTime())).with(TemporalAdjusters.previous(DayOfWeek.SUNDAY)).plusDays(1);
            prevDay=7;

        }
        else if("month".equals(type)){
            today = LocalDate.now().getDayOfMonth();

            prevStart = LocalDate.parse(formatter.format(c.getTime())).minusMonths(1).withDayOfMonth(1);
            prevFin = prevStart.withDayOfMonth(prevStart.lengthOfMonth()).plusDays(1);
            prevDay = prevStart.lengthOfMonth();

            c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));


        }
        System.out.println(prevStart +" "+prevFin+" "+prevDay);

        LocalDate ld = LocalDate.parse(formatter.format(c.getTime()), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDateTime ldt = ld.atStartOfDay();

        LocalDateTime prevStartT = prevStart.atStartOfDay();
        LocalDateTime prevFinT = prevFin.atStartOfDay();


        User user = new User();

        user.setUserId(userId);

        List<Walk> ret = walkRepository.findByUserAndDateAfter(user, ldt);

        List<Walk> prevRet = walkRepository.findByUserAndDateBetween(user,prevStartT,prevFinT);



        int sumTime=0,sumCalorie=0;
        double sumDistance=0;
        for(Walk w : ret){
            sumTime+=w.getTime();
            sumDistance+=w.getDistance();
            sumCalorie+=w.getCalorie();
        }

        int prevTime=0;
        for(Walk w : prevRet){
            System.out.println(w);
            prevTime+=w.getTime();
        }
        System.out.println(prevTime);

        timeResponseBody.setStatusCode(200);
        timeResponseBody.setMessage("OK");
        timeResponseBody.setSumTime(sumTime);
        timeResponseBody.setAvgTime((double)sumTime/today);
        timeResponseBody.setSumCalorie(sumCalorie);
        timeResponseBody.setAvgCalorie((double)sumCalorie/today);
        timeResponseBody.setSumDistance(sumDistance);
        timeResponseBody.setAvgDistance(sumDistance/today);
        timeResponseBody.setPrevSumTime(prevTime);
        timeResponseBody.setPrevAvgTime((double)prevTime/prevDay);

        return timeResponseBody;
    }

    @Override
    public void deleteRefreshToken(String userId) {
        redisService.deleteData(userId);
    }
}
