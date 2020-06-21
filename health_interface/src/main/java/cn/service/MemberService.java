package cn.service;

import cn.domain.Member;

import java.util.List;

public interface MemberService {
    //根据手机号查询该用户是否是会员
    Member findByTelephone(String telephone);
    //添加会员
    void add(Member member);

    List<Integer> findMemberCountByMonth(List<String> months);
}
