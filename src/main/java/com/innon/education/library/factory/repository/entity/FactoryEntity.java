package com.innon.education.library.factory.repository.entity;

import com.innon.education.code.controller.dto.CommonDTO;
import com.innon.education.library.factory.repository.model.Factory;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class FactoryEntity extends CommonDTO {
	private int document_id;            // 문서아이디
	private int location_id;            // 위치아이디
	private String display_user_id;     // 입고처리자
	private String display_date;          // 입고일
	private char delete_at;             // 삭제여부

	private int id;

	public FactoryEntity() {}

	public FactoryEntity(Factory factory) {
		this.document_id = factory.getDocument_id();
		this.location_id = factory.getLocation_id();
		// TODO : 로그인 유저 아이디로 변경 필요
		this.display_user_id = factory.getDisplay_user_id();
		this.display_date = factory.getDisplay_date();
		this.delete_at = factory.getDelete_at();
	}
}
