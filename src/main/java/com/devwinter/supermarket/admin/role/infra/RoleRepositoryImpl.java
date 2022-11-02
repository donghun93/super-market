package com.devwinter.supermarket.admin.role.infra;

import com.devwinter.supermarket.admin.role.command.domain.Role;
import com.devwinter.supermarket.admin.role.command.domain.RoleCustomRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.devwinter.supermarket.admin.role.command.domain.QRole.role;

@Repository
@RequiredArgsConstructor
public class RoleRepositoryImpl implements RoleCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Role> findByIdIsNotAndNameOrDesc(Long id, String name, String desc) {
        return Optional.ofNullable(queryFactory
                .select(role)
                .from(role)
                .where((role.name.eq(name).or(role.desc.eq(desc))), role.id.ne(id))
                .fetchOne());
    }
}
