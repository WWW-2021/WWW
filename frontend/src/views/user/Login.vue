<template>
  <div>
    <div>
      <div class="login-content">
        <div style="top-margin: 50px">
          <h2>환영합니다! 🙌</h2>
          <p>
            나의 산책 친구, <br />
            <span class="title">WWW</span> 와 함께 걸어보세요!
          </p>
        </div>
      </div>

      <img class="logo" src="@/assets/test.png" />
      <div>
        <a :href="kakaoUrl">
          <!-- <a
          href="https://kauth.kakao.com/oauth/authorize?client_id=1e31c0b3e807829e950f0236c26efec6&redirect_uri=http://localhost:8080/kakao/callback&response_type=code"
        > -->
          <img class="kakao" src="@/assets/kakao_login_medium_narrow.png" />
        </a>
      </div>
    </div>
  </div>
</template>

<script>
import("@/assets/style/Main.css");

export default {
  name: "Login",
  data() {
    return {
      kakaoUrl: "",
    };
  },
  mounted() {
    this.kakaoUrl =
      "https://kauth.kakao.com/oauth/authorize?client_id=" +
      process.env.VUE_APP_KAKAO_KEY +
      "&redirect_uri=https://j5a605.p.ssafy.io/kakao/callback&response_type=code";
  },
  methods: {},
};
</script>

<style scoped>
.login-content {
  text-align: left;
  margin: 80px 0px 20px 40px;
}
.login-content h2 {
  font-weight: bold;
}
.title {
  font-weight: bold;
}
.logo {
  margin-top: 80px;
  height: 200px;
}
.kakao {
  margin-top: 50px;
}
</style>
