package com.innon.education.bank.repository.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@Data
public class QuestionItemEntity {
    private int id;                     // SEQ
    private int qb_q_id;                // 문항아이디
    private String item_name;           // 보기
    private String correct_answer;      // 정답
    private int order_num;              // 순서
    private String use_yn;              // 사용여부(char형)
    private String delete_at;           // 삭제여부(char형)

    public QuestionItemEntity(int qb_q_id, Map<String, Object> mapEntity) {
        this.id = (Integer) mapEntity.getOrDefault("id", 0);
        this.qb_q_id = qb_q_id;
        this.item_name = String.valueOf(mapEntity.getOrDefault("item_name", ""));
        this.correct_answer = String.valueOf(mapEntity.getOrDefault("correct_answer", ""));
        this.order_num = (Integer) mapEntity.getOrDefault("order_num", 0);
        this.use_yn = String.valueOf(mapEntity.getOrDefault("use_yn", ""));
        this.delete_at = String.valueOf(mapEntity.getOrDefault("delete_at", ""));
    }

    public QuestionItemEntity(Map<String, Object> mapEntity) {
        this.id = (Integer) mapEntity.getOrDefault("id", 0);
        this.qb_q_id = (Integer) mapEntity.getOrDefault("qb_q_id", 0);
        this.item_name = String.valueOf(mapEntity.getOrDefault("item_name", ""));
        this.correct_answer = String.valueOf(mapEntity.getOrDefault("correct_answer", ""));
        this.order_num = (Integer) mapEntity.getOrDefault("order_num", 0);
        this.use_yn = String.valueOf(mapEntity.getOrDefault("use_yn", ""));
        this.delete_at = String.valueOf(mapEntity.getOrDefault("delete_at", ""));
    }

    public Boolean compareOfQuestionItem(Map<String, Object> itemMap) {
        boolean result = true;

        if(this.qb_q_id > 0) {
            result = this.qb_q_id == (Integer) itemMap.get("qb_q_id");
            result = result && this.item_name.equals(itemMap.get("item_name"));
            result = result && this.correct_answer.equals(itemMap.get("correct_answer"));
            result = result && this.order_num == (Integer) itemMap.get("order_num");
            result = result && this.use_yn.equals(itemMap.get("use_yn"));
        }
        result = result && this.delete_at.equals(itemMap.get("delete_at"));

        return result;
    }
}
