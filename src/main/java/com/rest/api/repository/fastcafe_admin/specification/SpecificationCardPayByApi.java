package com.rest.api.repository.fastcafe_admin.specification;

import com.rest.api.entity.fastcafe_admin.CardPayByApi;
import org.springframework.data.jpa.domain.Specification;

import java.sql.Date;

public class SpecificationCardPayByApi {

    public static Specification<CardPayByApi> eqaulBranchId(final int branch_id) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("branchId"), branch_id);
    }

    public static Specification<CardPayByApi> betweenTransDate(final Date startdate, final Date enddate){
        return (root, query, criteriaBuilder) -> criteriaBuilder.between(root.get("transDate"), startdate, enddate);
    }

    public static Specification<CardPayByApi> checkPayType(final String payType) {
        return (root, query, criteriaBuilder) -> {
          if(payType.equals("approve")) {
              return criteriaBuilder.greaterThanOrEqualTo(root.get("appAmt"), 0);
          } else if(payType.equals("cancel")) {
              return criteriaBuilder.lessThan(root.get("appAmt"), 0);
          }
          return null;
        };
    }
}
