package com.devwinter.supermarket.admin.member.query.response;

import com.devwinter.supermarket.admin.member.command.request.MemberRoleStatus;
import com.devwinter.supermarket.common.utils.StringConverter;
import com.devwinter.supermarket.member.command.domain.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminMemberDetailResponse {
    private Long memberRoleId;
    private String email;
    private String name;
    private String address;
    private String zipcode;
    private String gender;
    private String mannerPoint;
    private String deleteYn;
    private String createdDate;
    private MemberRoleStatus role;
    private String blockText;
    private boolean blockYn;

    public AdminMemberDetailResponse(Long id, String email, String name, String address, String detail, String zipcode,
                                     Gender gender, int mannerPoint, Boolean deleteYn, Boolean blockYn, LocalDateTime createdDate, LocalDateTime blockDate) {
        this.memberRoleId = id;
        this.email = email;
        this.name = name;
        this.zipcode = zipcode;
        this.address = getAddress(address, detail);
        this.mannerPoint = mannerPoint + " °C";
        this.gender = (gender != null) ? gender.getValue() : "";
        this.deleteYn = (deleteYn) ? "삭제" : "사용";
        this.blockYn = blockYn;
        this.blockText = getBlock(blockYn, blockDate);
        this.createdDate = StringConverter.localDateTimeToString(createdDate);
    }

    private String getAddress(String address, String detail) {
        String detailAddress = "";
        if (StringUtils.hasText(address)) {
            detailAddress += address;
        }
        if (StringUtils.hasText(detail)) {
            detailAddress += " - " + detail;
        }
        return detailAddress;
    }

    private String getBlock(Boolean block, LocalDateTime localDateTime) {
        if(block == null || !block) {
            return "미잠금";
        } else {
            return "잠금" + " (" + StringConverter.localDateTimeToString(localDateTime) + ")";
        }
    }
}
