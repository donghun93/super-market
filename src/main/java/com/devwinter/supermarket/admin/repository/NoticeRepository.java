package com.devwinter.supermarket.admin.repository;

import com.devwinter.supermarket.admin.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

}
