package com.innon.education.admin.system.sign.dao;

import com.innon.education.admin.system.sign.repository.*;

import java.util.List;

public interface SignDAO {
    int saveSign(Sign Sign);
    int saveSignUser(SignUser signUser);

    List<SignDTO> findSignList(Sign sign);
    List<SignUser> findSignUserList(int sign_id);
    SignDTO findSignById(int id);

    int savePlanSign(PlanSign planSign);
    int saveExtensionPlanSign(PlanSign planSign);
    int savePlanSignManager(PlanSignManager planSignManager);

    int updateSignApproveState(PlanSignManager planSignManager);
    PlanSignManager findTopApprover(PlanSignManager planSignManager);
    List<PlanSignManager> findApproverList(PlanSignManager planSignManager);

    int updateSign(Sign sign);
    int updateSignUser(SignUser signUser);

    int deleteSign(Sign sign);
    int deleteSignUser(SignUser signUser);

    int savePlanSignDetail(PlanSignDetail planSignDetail);

    List<PlanSignManager> findPlanSignManager(PlanSignManager planSignManager);

    int updatePlanSignManager(PlanSignManager planSignManager);
    int updatePlanSign(PlanSign planSign);

    int findPlanSignState(int plan_id);

    PlanSign findPlanSign(PlanSign paramSign);

    List<PlanSignManager> findPlanSignManagerExtensionRequest(PlanSignManager planSignManager);
}
