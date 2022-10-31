package com.devwinter.supermarket.admin.role.command.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoleId implements Serializable {

    @Column(name = "role_id")
    private Long id;

    public RoleId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoleId roleId = (RoleId) o;
        return Objects.equals(getId(), roleId.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
