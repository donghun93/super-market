package com.devwinter.supermarket.admin.domain;

import com.devwinter.supermarket.common.domain.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    private String name;

    private int orderNo;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> childs = new ArrayList<>();

    @Builder
    private Category(String name, int orderNo, Category parent) {
        this.name = name;
        this.orderNo = orderNo;
        this.parent = parent;
    }

    public void addCategory(Category child) {
        child.parent = this;
        this.childs.add(child);
    }
}
