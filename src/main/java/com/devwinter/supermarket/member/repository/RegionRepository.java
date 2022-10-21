package com.devwinter.supermarket.member.repository;


import com.devwinter.supermarket.member.domain.Member;
import com.devwinter.supermarket.member.domain.Region;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RegionRepository extends JpaRepository<Region, Long> {

    List<Region> findByMember(Member member);


    @EntityGraph(attributePaths = {"member"})
    Optional<Region> findWithMemberById(Long regionId);

}
