package com.innon.education.annual.plan.dao;

import com.innon.education.annual.plan.repository.dto.AnnualPlanDTO;
import com.innon.education.annual.plan.repository.dto.TbAnnualPlanDTO;
import com.innon.education.annual.plan.repository.entity.AnnualPlanEntity;
import com.innon.education.annual.plan.repository.entity.AnnualPlanJoinTable;
import com.innon.education.annual.plan.repository.entity.AnnualPlanMigrateEntity;
import com.innon.education.annual.plan.repository.entity.AnnualPlanSearchEntity;
import com.innon.education.management.plan.repository.model.ManagementPlan;

import java.util.List;

public interface AnnualPlanDAO {
    int saveAnnualPlan(AnnualPlanEntity plan);
    List<AnnualPlanDTO> findAnnualPlanList(AnnualPlanSearchEntity entity);
    AnnualPlanDTO findAnnualPlan(AnnualPlanSearchEntity annualPlan);
    int migrateAnnualPlan(AnnualPlanMigrateEntity entity);

    int findAnnualPlanListCnt(AnnualPlanSearchEntity entity);

    int saveAnnualPlanJoin(AnnualPlanJoinTable annualPlanJoinTable);

    int updateAnnualPlan(AnnualPlanEntity annualPlan);

    List<AnnualPlanDTO> annualEducationPlanList(AnnualPlanSearchEntity plan);

    int updateAnnualPlanJoin(AnnualPlanJoinTable delPlan);

    List<AnnualPlanDTO> annualList(AnnualPlanSearchEntity plan);

    int saveAnnualSaveYn(AnnualPlanEntity annualPlan);

    int saveAnnualSign(AnnualPlanEntity saveAnnualPlan);

    int findAnnualEducationMaxVersion(AnnualPlanSearchEntity plan);

    List<AnnualPlanDTO> findBeforeVersionAnnualEdu(AnnualPlanEntity searchAnnualEntity);

    List<TbAnnualPlanDTO> findBeforeVersionAnnualPlan(AnnualPlanEntity searchAnnualEntity);

    List<ManagementPlan> findBeforeVersionEducationPlan(AnnualPlanEntity searchAnnualEntity);
}
