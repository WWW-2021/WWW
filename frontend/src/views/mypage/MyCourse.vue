<template>
  <div id="main">
    <Header :showArrow="false" message="나의 산책 목록" id="navBar" />
    <div id="space"></div>
    <v-app style="height: 50px">
      <v-card>
        <v-tabs
          background-color="white"
          centered
          fixed-tabs
          slider-color="black"
          style="
            max-width: 425px;
            position: fixed;
            z-index: 99;
            left: 0;
            margin: 0 auto;
            right: 0;
          "
        >
          <v-tab
            v-on:click="getRecentCourse(userId)"
            style="font-size: 20px; color: gray; font-weight: bold"
            >최근 코스</v-tab
          >
          <v-tab
            v-on:click="getWishCourse(userId)"
            style="font-size: 20px; color: gray; font-weight: bold"
            >관심 코스</v-tab
          >
        </v-tabs>
      </v-card>
    </v-app>

    <div style="margin: 10px; text-algin: left; margin-top: 20px">
      <div v-if="!isRecent">
        <div v-for="(course, idx) in wishCourse.courseList" v-bind:key="idx">
          <div>
            <CourseCard
              @refresh-wish-course="getWishCourse"
              :isWish="true"
              :title="course.courseFlagName"
              :name="course.courseName"
              :courseId="course.courseId"
              :address="course.address"
              :km="course.courseLength"
              :min="course.time"
              :kcal="Math.round(course.timeInt * 60 * 0.06 * 10) / 10"
              :lat="course.latitude.toString()"
              :lng="course.longitude.toString()"
              :score="course.score"
              :detail="course.detail"
              :isBookmarked="true"
            />
          </div>
        </div>
      </div>
      <div v-if="isRecent">
        <div v-for="(course, idx) in recentCourse.courseList" v-bind:key="idx">
          <div>
            <ReviewCard
              @refresh-recent-course="getRecentCourse"
              :title="course.courseFlagName"
              :name="course.courseName"
              :courseId="course.courseId"
              :address="course.address"
              :km="course.courseLength.toFixed(2)"
              :min="timeText(course.time)"
              :kcal="course.calorie"
              :lat="course.latitude.toString()"
              :lng="course.longitude.toString()"
              :myScore="course.myScore"
              :detail="course.detail"
              :isBookmarked="course.myLike"
            />
          </div>
        </div>
      </div>
    </div>
    <div id="space"></div>
  </div>
</template>

<script>
import Header from "@/components/common/Header";
import("@/assets/style/Main.css");
import myCourseApi from "@/api/mycourse.js";
import ReviewCard from "@/views/mypage/ReviewCard";
import CourseCard from "@/views/course/CourseCard";

export default {
  name: "MyCourse",
  components: {
    Header,
    ReviewCard,
    CourseCard,
  },
  data() {
    return {
      isRecent: true,
      curID: "",
      userId: this.$store.getters.getLoginUserInfo.userId,
      recentCourse: [],
      wishCourse: [],
    };
  },
  mounted() {
    this.$store.commit("SET_PREV_PAGE", "/user/mycourse");
    this.getRecentCourse(this.userId);
    this.$store.commit("SET_IS_NOT_INDEX");
  },
  methods: {
    async getWishCourse(userId) {
      let data = {
        type: "wish",
        userId: userId,
      };
      this.isRecent = false;
      this.wishCourse = await myCourseApi.getCourseData(data, {});
    },
    async getRecentCourse(userId) {
      let data = {
        type: "recent",
        userId: userId,
      };
      this.isRecent = true;
      this.recentCourse = await myCourseApi.getCourseData(data, {});
    },
    timeText(time) {
      var t = parseInt(time);
      var text = "";
      if (t >= 3600) text += parseInt(t / 3600) + "시간 ";
      if (t >= 60) text += parseInt((t % 3600) / 60) + "분 ";
      text += parseInt((t % 3600) % 60) + "초";
      return text;
    },
  },
};
</script>

<style>
#space {
  height: 50px;
}

#main {
  width: 100%;
  max-width: 425px;
  top: 0;
  left: 0;
  right: 0;
  margin: auto;
  bottom: 0;
  background: #ffffff;
}

.v-application--wrap {
  max-height: 425px;
  min-height: 50px;
}
</style>
