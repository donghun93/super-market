package com.devwinter.supermarket.member.response;

import com.devwinter.supermarket.member.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class MemberListResponse {

    private Long totalCount;
    private List<MemberSimpleResponse> members = new ArrayList<>();

    @Builder
    private MemberListResponse(Long totalCount, List<MemberSimpleResponse> members) {
        this.totalCount = totalCount;
        this.members = members;
    }

    public static MemberListResponse of(List<Member> lists) {
        return MemberListResponse.builder()
                .totalCount((long) lists.size())
                .members(lists.stream().map(m-> MemberSimpleResponse.builder()
                        .id(m.getId())
                        .suspensionYn(suspensionYnToString(m.getSuspensionYn()))
                        .useYn(useYnToString(m.getUseYn()))
                        .lastLoginDate(localDateTimeToString(m.getLastLoginDate()))
                        .resignDate(localDateTimeToString(m.getLastLoginDate()))
                        .name(m.getPersonalInformation().getName())
                        .joinDate(localDateTimeToString(m.getCreatedDate()))
                        .build()).collect(Collectors.toList()))
                .build();
    }

    private static String suspensionYnToString(Boolean suspensionYn) {
        return (suspensionYn) ? "정지" : "사용";
    }

    private static String useYnToString(Boolean suspensionYn) {
        return (suspensionYn) ? "가입" : "탈퇴";
    }

    private static String localDateTimeToString(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");
        return localDateTime != null ? localDateTime.format(formatter) : "";
    }
}
