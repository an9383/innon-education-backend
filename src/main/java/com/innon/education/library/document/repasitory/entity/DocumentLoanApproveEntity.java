package com.innon.education.library.document.repasitory.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class DocumentLoanApproveEntity {
    private int loan_id;
    private int role_id;
    private String approve_state;

    public DocumentLoanApproveEntity(
            int loan_id,
            int role_id,
            String approve_state
    ) {
        this.loan_id = loan_id;
        this.role_id = role_id;
        this.approve_state = approve_state;
    }
}
