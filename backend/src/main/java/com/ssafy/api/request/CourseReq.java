package com.ssafy.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("CourseReq")
public class CourseReq {
    String userId;

    @Schema(description = "페이지", defaultValue = "0", example = "0")
    int page;

    @Schema(description = "페이지 크기", defaultValue = "10", example = "10")
    int size;

    @Schema(description = "distance(거리순), likes(좋아요순)", defaultValue = "distance", example = "distance")
    String sort;

    @Schema(description = "코스 목록 찾기 조건(dong = 동 검색, likes = 로그인 사용자의 좋아요 코스", defaultValue = "dong", example = "dong")
    String criteria;

    @Schema(description = "최소 시간(분)", defaultValue = "0", example = "0")
    int minTime;

    @Schema(description = "최대 시간(분)", defaultValue = "1440", example = "1440")
    int maxTime;

    @Schema(description = "최소 산책로 길이(KM)", defaultValue = "0", example = "0")
    int minDistnace;

    @Schema(description = "최대 산책로 길이(KM)", defaultValue = "10", example = "10")
    int maxDistance;

    String dong;

    Double longtitude;

    Double latitude;
}
