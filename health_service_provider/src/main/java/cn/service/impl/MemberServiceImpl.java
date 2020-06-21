package cn.service.impl;

import cn.dao.MemberMapper;
import cn.domain.Member;
import cn.service.MemberService;
import cn.utils.MD5Utils;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberMapper memberMapper;
    @Override
    public Member findByTelephone(String telephone) {
        return memberMapper.findByTelephone(telephone);
    }

    @Override
    public void add(Member member) {
        if (member.getPassword()!=null){
                member.setPassword(MD5Utils.md5(member.getPassword()));
        }
        memberMapper.add(member);
    }
    //根据月份查询本月会员数量
    @Override
    public List<Integer> findMemberCountByMonth(List<String> months) {
        List<Integer> list = new ArrayList<>();
        for (String month : months) {
            String s =month + ".31";
            Integer count = memberMapper.findMemberCountBeforeDate(s);
            list.add(count);
        }

        return list;
    }
}
