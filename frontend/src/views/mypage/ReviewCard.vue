<template>
  <div class="card">
    <el-row style="display: flex; align-items: center">
      <el-col :span="19" style="text-align: left">
        <div @click="goDetail()">
          <p
            class="title"
            style="
              overflow: hidden;
              text-overflow: ellipsis;
              white-space: nowrap;
            "
          >
            {{ $props.title }}
            <span v-if="$props.title !== $props.name">- {{ $props.name }}</span>
          </p>
          <p class="content">
            <i class="el-icon-location" style="color: #ee684a" />{{
              $props.address
            }}
          </p>
          <p class="content">
            {{ $props.km }}km | {{ $props.min }} | {{ $props.kcal }}kcal
          </p>
        </div>
      </el-col>
      <el-col :span="5" style="text-align: center">
        <el-button
          size="mini"
          v-if="$props.myScore == 0"
          @click="clickBox($props.courseId)"
          style="
            border: 1px solid #49ab76;
            background-color: #49ab76;
            color: #ffffff;
            border-radius: 10px;
          "
          >리뷰<br />작성</el-button
        >
        <div v-if="$props.myScore != 0">
          <i class="el-icon-star-on"></i>{{ $props.myScore }}
        </div>
      </el-col>
    </el-row>
    <div>
      <el-dialog
        :visible.sync="dialogVisible"
        width="70%"
        center
        :show-close="false"
      >
        <div style="text-align: center; display: flex; justify-content: center">
          <star-rating
            :increment="0.5"
            v-model="rating"
            @current-rating="setRating"
            :show-rating="false"
            :star-size="40"
          >
          </star-rating>
        </div>
        <div
          style="
            font-size: 15pt;
            text-align: center;
            font-weight: 700;
            padding-top: 20px;
          "
        >
          {{ $props.title }}<br />
          <span v-if="$props.title !== $props.name">{{ $props.name }}</span>
        </div>
        <div
          style="
            font-size: 9pt;
            text-align: center;

            padding-top: 10px;
          "
        >
          걸어보시니 어떠셨나요? <br />솔직한 별점⭐을 남겨주세요! <br />
          한 번 작성하신 리뷰는 수정할 수 없어요
        </div>
        <div slot="footer" class="dialog-footer" style="padding-top: 0px">
          <el-button
            v-if="this.rating > 0"
            type="danger"
            style="
              border: 4px solid #49ab76;
              width: 80%;
              background-color: #49ab76;
              border-radius: 30px;

              padding-top: 10px;
              padding-bottom: 10px;
            "
            @click="sendReview($props.courseId)"
            >✨ 제출하기 ✨</el-button
          ><br />
          <el-button
            v-if="this.rating == 0"
            disabled
            type="danger"
            style="
              cursor: not-allowed;
              border: 4px solid #49ab76;
              width: 80%;
              background-color: #49ab76;
              border-radius: 30px;

              padding-top: 10px;
              padding-bottom: 10px;
            "
            @click="sendReview($props.courseId)"
            >✨ 제출하기 ✨</el-button
          ><br />
          <el-button
            type="danger"
            style="
              border: 4px solid #ffffff;
              width: 80%;
              background-color: #ffffff;
              color: #49ab76;
              border-radius: 30px;

              padding-top: 10px;
              padding-bottom: 10px;
            "
            @click="dialogVisible = false"
            >🙅‍♂️ 다음에 할게요 🙅‍♀️</el-button
          >
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import router from "@/router/index.js";
import axios from "@/utils/axios.js";
import StarRating from "vue-star-rating";

export default {
  name: "CourseCard",
  components: {
    StarRating,
  },
  data() {
    return {
      dialogVisible: false,
      rating: 1,
      isRecent: true,
      userId: this.$store.getters.getLoginUserInfo.userId,
      isLiked: this.$props.isBookmarked,
    };
  },
  props: {
    title: {
      type: String,
      default: "title",
    },
    name: {
      type: String,
      defalut: "name",
    },
    courseId: {
      type: Number,
      default: 0,
    },
    address: {
      type: String,
      default: "address",
    },
    km: {
      type: Number,
      default: 0,
    },
    min: {
      type: String,
      default: "min",
    },
    kcal: {
      type: Number,
      default: 0,
    },
    myScore: {
      type: Number,
    },
    lat: {
      type: String,
      default: "37.4265296",
    },
    lng: {
      type: String,
      default: "126.986664",
    },
    detail: {
      type: String,
      default: "test",
    },
    isBookmarked: {
      type: Boolean,
      default: false,
    },
  },
  methods: {
    sendReview(id) {
      axios
        .post("/review/", {
          courseId: id,
          score: this.rating,
          userId: this.userId,
        })
        .then(() => {
          this.rating = 1;
          this.dialogVisible = false;
          this.$emit("refresh-recent-course", this.userId);
        });
    },
    // 산책로 세부 정보를 가져오기
    async goDetail() {
      await axios
        .get("/course/", {
          params: {
            courseId: this.$props.courseId,
            userId: this.$store.getters.getLoginUserInfo.userId,
          },
        })
        .then((res) => {
          this.$store.commit("SET_CUR_COURSE", {
            id: this.$props.courseId,
            title:
              this.$props.title != this.$props.name
                ? this.$props.title + "-" + this.$props.name
                : this.$props.title,
            address: this.$props.address,
            lat: this.$props.lat,
            lng: this.$props.lng,
            score: this.$props.score,
            distance: this.$props.km,
            time: this.$props.min,
            kcal: this.$props.kcal,
            detail: this.$props.detail,
            cafe: res.data.cafeList,
            conv: res.data.convList,
            isBookmarked: res.data.myLike,
          });
        });
      router.push("/course/detail");
    },
    clickBox(id) {
      this.curID = id;
      this.dialogVisible = true;
    },
  },
};
</script>

<style scoped>
.card {
  margin-bottom: 30px;
  height: 90px;
  background: #f6f6f6;
  border-radius: 20px;
  padding: 15px;
}

.title {
  font-size: 15pt;
  font-weight: 600;
}

.content {
  font-size: 11pt;
  color: #6f7789;
}
</style>
