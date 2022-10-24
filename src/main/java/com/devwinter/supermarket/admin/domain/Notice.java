package com.devwinter.supermarket.admin.domain;

import com.devwinter.supermarket.common.domain.BaseEntity;
import com.devwinter.supermarket.member.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notice extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_id")
    private Long id;

    private String title;

    @Lob
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private Boolean showYn;
    private Boolean deleteYn;

    @Builder
    private Notice(String title, String content, Member member) {
        this.title = title;
        this.content = content;
        this.showYn = true;
        this.deleteYn = false;
        this.member = member;
    }

    public void updateNotice(String title, String content, Member member) {
        this.title = title;
        this.content = content;
        this.member = member;
    }

    public void deleteNotice() {
        this.deleteYn = true;
    }

    public void changeShow(Boolean showYn) {
        this.showYn = showYn;
    }
}
