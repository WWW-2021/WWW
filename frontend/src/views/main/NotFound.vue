<template>
  <div>
    <div
      class="default"
      style="
        padding-top: 40%;
        margin-bottom: 10px;
        display: flex;
        justify-content: center;
      "
    ></div>
    <p style="font-size: 60pt; color: #ee684a; font-weight: 800">404</p>
    <span style="font-size: 12pt">페이지를 찾을 수 없습니다</span>
  </div>
</template>

<script>
import axios from "axios";
import router from "@/router/index.js";

import("@/assets/style/Main.css");

export default {
  name: "Main",
  components: {},
  data() {
    return {
      lat: "",
      lng: "",

      dong: "",
      isAgree: false,
    };
  },
  mounted() {
    this.$store.commit("SET_IS_NOT_INDEX");
  },
  methods: {
    goMain() {
      this.geofind();
    },
    geofind() {
      if (!("geolocation" in navigator)) {
        return;
      }

      // get position
      navigator.geolocation.watchPosition(
        (pos) => {
          this.lat = pos.coords.latitude;
          this.lng = pos.coords.longitude;

          axios
            .get(
              "https://dapi.kakao.com/v2/local/geo/coord2regioncode.json?x=" +
                this.lng +
                "&y=" +
                this.lat,
              {
                headers: {
                  Authorization: "KakaoAK bacd72f58ac01490602415c683ad8c05",
                },
              }
            )
            .then((response) => {
              this.dong = response.data.documents[0].region_3depth_name;
              this.$store.commit("SET_USER_LOCATION", {
                lat: this.lat,
                lng: this.lng,
                dong: this.dong,
              });
              router.push("/main");
            });
        },
        (err) => {
          this.textContent = err.message;
        }
      );
    },
  },
};
</script>

<style scoped>
.main-top {
  height: 200px;
}

.main-box {
  margin-bottom: 15px;
  height: 90px;
  background: #f6f6f6;
  border-radius: 20px;
}
</style>
