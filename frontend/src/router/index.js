import Vue from "vue";
import VueRouter from "vue-router";

// 메인
import Main from "@/views/main/Main";

// 코스 정보
import Course from "@/views/course/Course";

// 유저 산책 정보
import MyWalk from "@/views/user/Walk";

// 로그인
import Login from "@/views/user/Login";

// Redirect
import KakaoCallback from "@/views/user/kakao-callback";

// 회원가입
import SignUp from "@/views/user/Signup";

Vue.use(VueRouter);

const routes = [
  { path: "/index.html", component: Main, alias: "/" },
  // 메인
  { path: "", redirect: "/main" },
  { path: "/main", name: "Main", component: Main },

  // 동네 코스 목록
  { path: "/course", component: Course },

  // 나의 산책 분석
  { path: "/user/walk", component: MyWalk },

  // 로그인
  { path: "/login", component: Login },

  // Redirect
  { path: "/kakao/callback", component: KakaoCallback },

  // 회원가입
  { path: "/signup", component: SignUp },
];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes,
});

export default router;
