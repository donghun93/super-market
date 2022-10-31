package com.devwinter.supermarket.admin.repository.impl;

import com.devwinter.supermarket.admin.domain.Notice;
import com.devwinter.supermarket.admin.exception.NoticeErrorCode;
import com.devwinter.supermarket.admin.exception.NoticeException;
import com.devwinter.supermarket.admin.repository.NoticeQueryRepository;
import com.devwinter.supermarket.admin.repository.NoticeRepository;
import com.devwinter.supermarket.admin.response.NoticeSimpleResponse;
import com.devwinter.supermarket.member.domain.Member;
import com.devwinter.supermarket.member.domain.value.Birth;
import com.devwinter.supermarket.member.domain.value.PersonalInformation;
import com.devwinter.supermarket.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;

@SpringBootTest
@ActiveProfiles({"test"})
@Transactional
class NoticeQueryRepositoryImplTest {

    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private EntityManager em;


}