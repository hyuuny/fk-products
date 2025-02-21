package com.hyuuny.fkproducts.products.domain;

import com.hyuuny.fkproducts.products.service.ProductDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.hyuuny.fkproducts.products.domain.QProductEntity.productEntity;

@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ProductEntity> search(ProductDto.ProductSearchCondition searchCondition, Pageable pageable) {
        // query
        JPAQuery<ProductEntity> query = queryFactory
                .selectFrom(productEntity)
                .where(
                        nameEq(searchCondition.name())
                );

        // count
        JPAQuery<Long> countQuery = queryFactory
                .select(productEntity.count())
                .from(productEntity)
                .where(
                        nameEq(searchCondition.name())
                );

        List<ProductEntity> content = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long totalCount = countQuery.fetchOne();
        totalCount = totalCount != null ? totalCount : 0L;

        return new PageImpl<>(content, pageable, totalCount);
    }

    private BooleanExpression nameEq(String name) {
        return (name == null || name.isEmpty()) ? null : productEntity.name.eq(name);
    }
}
