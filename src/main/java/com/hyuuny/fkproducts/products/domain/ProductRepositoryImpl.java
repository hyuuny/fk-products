package com.hyuuny.fkproducts.products.domain;

import com.hyuuny.fkproducts.products.service.ProductDto;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

import static com.hyuuny.fkproducts.products.domain.QProductEntity.productEntity;

@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ProductEntity> search(ProductDto.ProductSearchCondition searchCondition, Pageable pageable) {
        // query
        List<ProductEntity> content = queryFactory
                .selectFrom(productEntity)
                .where(
                        nameEq(searchCondition.name())
                )
                .orderBy(getOrderSpecifiers(pageable.getSort()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // count
        JPAQuery<Long> countQuery = queryFactory
                .select(productEntity.count())
                .from(productEntity)
                .where(
                        nameEq(searchCondition.name())
                );

        Long totalCount = countQuery.fetchOne();
        totalCount = totalCount != null ? totalCount : 0L;

        return new PageImpl<>(content, pageable, totalCount);
    }

    private BooleanExpression nameEq(String name) {
        return (name == null || name.isEmpty()) ? null : productEntity.name.eq(name);
    }

    private OrderSpecifier<?>[] getOrderSpecifiers(Sort sort) {
        ArrayList<OrderSpecifier<?>> orders = new ArrayList<>();

        for (Sort.Order order : sort) {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            PathBuilder<? extends ProductEntity> pathBuilder = new PathBuilder<>(productEntity.getType(), productEntity.getMetadata());
            orders.add(new OrderSpecifier<>(direction, pathBuilder.getString(order.getProperty())));
        }
        return orders.toArray(new OrderSpecifier[0]);
    }
}
