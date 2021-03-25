package com.company.dao;

import com.company.pojo.Member;

public interface MemberDao {
    Member getMemberByTelephone(String telephone);

    void add(Member member);

    Integer getMemberCountByMonth(String lastDayOfMonth);

    int getTodayNewMember(String today);

    int getTotalMember();

    int getThisWeekAndMonthNewMember(String weekMonday);


}
