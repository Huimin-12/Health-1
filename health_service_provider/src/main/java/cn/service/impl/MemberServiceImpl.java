package cn.service.impl;

import cn.dao.MemberMapper;
import cn.domain.Member;
import cn.service.MemberService;
import cn.utils.MD5Utils;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

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
}
