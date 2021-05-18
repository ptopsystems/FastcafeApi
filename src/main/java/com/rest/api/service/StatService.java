package com.rest.api.service;

import com.rest.api.entity.fastcafe_stat.StatDailyCardPayByApi;
import com.rest.api.entity.fastcafe_stat.StatMonthlyCardPayByApi;
import com.rest.api.entity.fastcafe_stat.StatWeeklyCardPayByApi;
import com.rest.api.entity.fastcafe_stat.dto.*;
import com.rest.api.repository.fastcafe_stat.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.sql.Date;
import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatService {

    private final StatVanPayDailyRepository statVanPayDailyRepository;
    private final StatVanPayWeeklyRepository statVanPayWeeklyRepository;
    private final StatVanPayMonthlyRepository statVanPayMonthlyRepository;

    private final StatDailyCardPayByApiRepository statDailyCardPayByApiRepository;
    private final StatWeeklyCardPayByApiRepository statWeeklyCardPayByApiRepository;
    private final StatMonthlyCardPayByApiRepository statMonthlyCardPayByApiRepository;

    @Transactional
    public Date getMaxIndexRegdateForStatVanPayDailyByBranchId(int branch_id) {
        return statVanPayDailyRepository.getMaxIndexRegdate(branch_id);
    }

    @Transactional
    public List<IStatVanPayDailyGroupDTO> listStatVanPayDailyGroupByBranchId(int branch_id, Date startdate, Date enddate) {
        return statVanPayDailyRepository.listGroupByBranchId(branch_id, startdate, enddate);
    }

    @Transactional
    public List<StatVanPayWeeklyTotalStatDTO> listStatVanPayWeeklyGroupByBranchId(int branch_id, String baseYear, String baseMonth, String machineType, int branch_machine_id){

        String pastBaseYear = "";
        String pastBaseMonth = "";
        if(baseMonth.equals("01")){
            pastBaseYear = String.valueOf(Integer.parseInt(baseYear) - 1);
            pastBaseMonth = "12";
        } else {
            pastBaseYear = baseYear;
            pastBaseMonth = String.valueOf(Integer.parseInt(baseMonth) - 1 < 10 ? "0" + (Integer.parseInt(baseMonth) - 1) : Integer.parseInt(baseMonth) - 1);
        }

        List<IStatVanPayWeeklyGroupDTO> current = null;
        List<IStatVanPayWeeklyGroupDTO> past = null;
        if(branch_machine_id == 0){
            if(StringUtils.hasText(machineType)){
                current = statVanPayWeeklyRepository.listGroupByBaseWeek(branch_id, baseYear, baseMonth, machineType);
                past = statVanPayWeeklyRepository.listGroupByBaseWeek(branch_id, pastBaseYear, pastBaseMonth, machineType);
            } else {
                current = statVanPayWeeklyRepository.listGroupByBaseWeek(branch_id, baseYear, baseMonth);
                past = statVanPayWeeklyRepository.listGroupByBaseWeek(branch_id, pastBaseYear, pastBaseMonth);
            }
        } else {
            current = statVanPayWeeklyRepository.listGroupByBaseWeek(branch_id, baseYear, baseMonth, branch_machine_id);
            past = statVanPayWeeklyRepository.listGroupByBaseWeek(branch_id, pastBaseYear, pastBaseMonth, branch_machine_id);
        }

        List<StatVanPayWeeklyTotalStatDTO> stats = new LinkedList<>();
        int baseWeek = 1;
        if(past.size() > current.size()){
            for(IStatVanPayWeeklyGroupDTO p : past){
                StatVanPayWeeklyTotalStatDTO dto = new StatVanPayWeeklyTotalStatDTO();
                dto.setBaseWeek(baseWeek++ + "주");
                dto.setPastStartdate(p.getStartdate());
                dto.setPastEnddate(p.getEnddate());
                dto.setPastMoney(p.getPayMoney());
                dto.setPastCnt(p.getPayCnt());
                stats.add(dto);
            }

            for(int i=0; i< current.size(); i++){
                StatVanPayWeeklyTotalStatDTO stat = stats.get(i);
                stat.setStartdate(current.get(i).getStartdate());
                stat.setEnddate(current.get(i).getEnddate());
                stat.setCurrMoney(current.get(i).getPayMoney());
                stat.setCurrCnt(current.get(i).getPayCnt());
            }
        } else {
            for(IStatVanPayWeeklyGroupDTO c : current){
                StatVanPayWeeklyTotalStatDTO dto = new StatVanPayWeeklyTotalStatDTO();
                dto.setBaseWeek(baseWeek++ + "주");
                dto.setStartdate(c.getStartdate());
                dto.setEnddate(c.getEnddate());
                dto.setCurrMoney(c.getPayMoney());
                dto.setCurrCnt(c.getPayCnt());
                stats.add(dto);
            }

            for(int i=0; i< past.size(); i++){
                StatVanPayWeeklyTotalStatDTO stat = stats.get(i);
                stat.setPastStartdate(past.get(i).getStartdate());
                stat.setPastEnddate(past.get(i).getEnddate());
                stat.setPastMoney(past.get(i).getPayMoney());
                stat.setPastCnt(past.get(i).getPayCnt());
            }
        }

        return stats;
    }

    @Transactional
    public List<StatVanPayMonthlyTotalStatDTO> listStatVanPayMonthlyGroupByBranchId(int branch_id, String baseYear, String baseMonth, String machineType, int branch_machine_id) {
        String pastBaseYear = String.valueOf(Integer.parseInt(baseYear) - 1);

        List<IStatVanPayMonthlyGroupDTO> current = null;
        List<IStatVanPayMonthlyGroupDTO> past = null;
        if(branch_machine_id == 0){
            if(StringUtils.hasText(machineType)){
                current = statVanPayMonthlyRepository.groupByBaseWeek(branch_id, baseYear, baseMonth, machineType);
                past = statVanPayMonthlyRepository.groupByBaseWeek(branch_id, pastBaseYear, baseMonth, machineType);
            } else {
                current = statVanPayMonthlyRepository.groupByBaseWeek(branch_id, baseYear, baseMonth);
                past = statVanPayMonthlyRepository.groupByBaseWeek(branch_id, pastBaseYear, baseMonth);
            }
        } else {
            current = statVanPayMonthlyRepository.groupByBaseWeek(branch_id, baseYear, baseMonth, branch_machine_id);
            past = statVanPayMonthlyRepository.groupByBaseWeek(branch_id, pastBaseYear, baseMonth, branch_machine_id);
        }

        List<StatVanPayMonthlyTotalStatDTO> stats = new LinkedList<>();
        if(past.size() > current.size()){
            for(IStatVanPayMonthlyGroupDTO p : past){
                StatVanPayMonthlyTotalStatDTO dto = new StatVanPayMonthlyTotalStatDTO();
                dto.setPastMoney(p.getPayMoney());
                dto.setPastCnt(p.getPayCnt());
                stats.add(dto);
            }

            for(int i=0; i< current.size(); i++){
                StatVanPayMonthlyTotalStatDTO stat = stats.get(i);
                stat.setBaseYear(current.get(i).getBaseYear());
                stat.setBaseMonth(current.get(i).getBaseMonth());
                stat.setCurrMoney(current.get(i).getPayMoney());
                stat.setCurrCnt(current.get(i).getPayCnt());
            }
        } else {
            for(IStatVanPayMonthlyGroupDTO c : current){
                StatVanPayMonthlyTotalStatDTO dto = new StatVanPayMonthlyTotalStatDTO();
                dto.setBaseYear(c.getBaseYear());
                dto.setBaseMonth(c.getBaseMonth());
                dto.setCurrMoney(c.getPayMoney());
                dto.setCurrCnt(c.getPayCnt());
                stats.add(dto);
            }

            for(int i=0; i< past.size(); i++){
                StatVanPayMonthlyTotalStatDTO stat = stats.get(i);
                stat.setPastMoney(past.get(i).getPayMoney());
                stat.setPastCnt(past.get(i).getPayCnt());
            }
        }

        return stats;    }

    @Transactional
    public IStatVanPayDailyGroupDTO getStatVanPayDailyGroupByBranchId(int branch_id, Date startdate, Date enddate) {
        return statVanPayDailyRepository.getGroupByBranchId(branch_id, startdate, enddate);
    }

    @Transactional
    public IStatVanPayWeeklyGroupDTO getStatVanPayWeeklyGroupByBranchId(int branch_id, Date basedate) {
        return statVanPayWeeklyRepository.getGroupByBranchId(branch_id, basedate);
    }

    @Transactional
    public IStatVanPayDailyGroupDTO getStatVanPayDaily(int branch_id, Date basedate) {
        return statVanPayDailyRepository.findByBranchIdAndIndexRegdate(branch_id, basedate);
    }

    @Transactional
    public Date getMaxIndexRegdateForStatVanPayWeekly(int branchId, String year, String month) {
        return statVanPayWeeklyRepository.getMaxIndexRegdateByBranchIdAndBaseYearAndBaseMonth(branchId, year, month);
    }

    @Transactional
    public List<StatDailyCardPayByApi> listStatDailyCardPayByApi(int branchId, Date startdate, Date enddate) {
        return statDailyCardPayByApiRepository.findByBranchIdAndIndexRegdateBetweenOrderByIndexRegdateDesc(branchId, startdate, enddate);
    }

    @Transactional
    public Date getMaxIndexRegdateForStatDailyCardPayByApi(int branchId) {
        return statDailyCardPayByApiRepository.getMaxIndexRegdateByBranchId(branchId);
    }

    @Transactional
    public List<StatWeeklyCardPayByApiDTO> listStatWeeklyCardPayByApi(int branch_id, String baseYear, String baseMonth) {
        String pastBaseYear = "";
        String pastBaseMonth = "";
        if(baseMonth.equals("01")){
            pastBaseYear = String.valueOf(Integer.parseInt(baseYear) - 1);
            pastBaseMonth = "12";
        } else {
            pastBaseYear = baseYear;
            pastBaseMonth = String.valueOf(Integer.parseInt(baseMonth) - 1 < 10 ? "0" + (Integer.parseInt(baseMonth) - 1) : Integer.parseInt(baseMonth) - 1);
        }

        List<StatWeeklyCardPayByApi> current = statWeeklyCardPayByApiRepository.findByBranchIdAndBaseYearAndBaseMonth(branch_id, baseYear, baseMonth);
        List<StatWeeklyCardPayByApi> past = statWeeklyCardPayByApiRepository.findByBranchIdAndBaseYearAndBaseMonth(branch_id, pastBaseYear, pastBaseMonth);;


        List<StatWeeklyCardPayByApiDTO> stats = new LinkedList<>();
        int baseWeek = 1;
        if(past.size() > current.size()){
            for(StatWeeklyCardPayByApi p : past){
                StatWeeklyCardPayByApiDTO dto = new StatWeeklyCardPayByApiDTO();
                dto.setBaseWeek(baseWeek++ + "주");
                dto.setPastStartdate(p.getStartdate());
                dto.setPastEnddate(p.getEnddate());
                dto.setPastTotal(p.getTotal());
                dto.setPastCnt(p.getTotalCnt());
                stats.add(dto);
            }

            for(int i=0; i< current.size(); i++){
                StatWeeklyCardPayByApiDTO stat = stats.get(i);
                stat.setStartdate(current.get(i).getStartdate());
                stat.setEnddate(current.get(i).getEnddate());
                stat.setCurrTotal(current.get(i).getTotal());
                stat.setCurrCnt(current.get(i).getTotalCnt());
            }
        } else {
            for(StatWeeklyCardPayByApi c : current){
                StatWeeklyCardPayByApiDTO dto = new StatWeeklyCardPayByApiDTO();
                dto.setBaseWeek(baseWeek++ + "주");
                dto.setStartdate(c.getStartdate());
                dto.setEnddate(c.getEnddate());
                dto.setCurrTotal(c.getTotal());
                dto.setCurrCnt(c.getTotalCnt());
                stats.add(dto);
            }

            for(int i=0; i< past.size(); i++){
                StatWeeklyCardPayByApiDTO stat = stats.get(i);
                stat.setPastStartdate(past.get(i).getStartdate());
                stat.setPastEnddate(past.get(i).getEnddate());
                stat.setPastTotal(past.get(i).getTotal());
                stat.setPastCnt(past.get(i).getTotalCnt());
            }
        }

        return stats;
    }

    @Transactional
    public List<StatMonthlyCardPayByApiDTO> listStatMonthlyCardPayByApi(int branchId, String baseYear, String baseMonth) {

        String pastBaseYear = String.valueOf(Integer.parseInt(baseYear) - 1);

        List<StatMonthlyCardPayByApi> current = statMonthlyCardPayByApiRepository.findByBranchIdAndBaseYearAndBaseMonth(branchId, baseYear, baseMonth);
        List<StatMonthlyCardPayByApi> past = statMonthlyCardPayByApiRepository.findByBranchIdAndBaseYearAndBaseMonth(branchId, pastBaseYear, baseMonth);;

        List<StatMonthlyCardPayByApiDTO> stats = new LinkedList<>();
        if(past.size() > current.size()){
            for(StatMonthlyCardPayByApi p : past){
                StatMonthlyCardPayByApiDTO dto = new StatMonthlyCardPayByApiDTO();
                dto.setPastTotal(p.getTotal());
                dto.setPastCnt(p.getTotalCnt());
                stats.add(dto);
            }

            for(int i=0; i< current.size(); i++){
                StatMonthlyCardPayByApiDTO stat = stats.get(i);
                stat.setBaseYear(current.get(i).getBaseYear());
                stat.setBaseMonth(current.get(i).getBaseMonth());
                stat.setCurrTotal(current.get(i).getTotal());
                stat.setCurrCnt(current.get(i).getTotalCnt());
            }
        } else {
            for(StatMonthlyCardPayByApi c : current){
                StatMonthlyCardPayByApiDTO dto = new StatMonthlyCardPayByApiDTO();
                dto.setBaseYear(c.getBaseYear());
                dto.setBaseMonth(c.getBaseMonth());
                dto.setCurrTotal(c.getTotal());
                dto.setCurrCnt(c.getTotalCnt());
                stats.add(dto);
            }

            for(int i=0; i< past.size(); i++){
                StatMonthlyCardPayByApiDTO stat = stats.get(i);
                stat.setPastTotal(past.get(i).getTotal());
                stat.setPastCnt(past.get(i).getTotalCnt());
            }
        }

        return stats;
    }

    public List<StatMonthlyCardPayByApi> getStatMonthlyCardPayByApi(int branchId, String baseYear, String baseMonth) {
        return statMonthlyCardPayByApiRepository.findByBranchIdAndBaseYearAndBaseMonth(branchId, baseYear, baseMonth);
    }

    public StatWeeklyCardPayByApi getStatWeeklyCardPayByApi(int branch_id, Date basedate) {
        return statWeeklyCardPayByApiRepository.findByIdAndBasedateBetweenStartdateAndEnddate(branch_id, basedate);
    }

    public StatMonthlyCardPayByApi getOneStatMonthlyCardPayByApi(int branch_id, String baseYear, String baseMonth) {
        return statMonthlyCardPayByApiRepository.findOneByBranchIdAndBaseYearAndBaseMonth(branch_id, baseYear, baseMonth);
    }

    public StatDailyCardPayByApi getStatDailyCardPayByApi(int branch_id, Date basedate) {
        return statDailyCardPayByApiRepository.findByBranchIdAndIndexRegdate(branch_id, basedate);
    }
}
